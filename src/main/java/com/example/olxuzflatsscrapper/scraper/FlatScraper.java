package com.example.olxuzflatsscrapper.scraper;

import com.example.olxuzflatsscrapper.converter.FlatConverter;
import com.example.olxuzflatsscrapper.dto.FlatDto;
import com.example.olxuzflatsscrapper.dto.FlatDto.FlatDtoBuilder;
import com.example.olxuzflatsscrapper.repository.FlatRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlatScraper {

    private final FlatRepository flatRepository;
    private final RestTemplate restTemplate;
    private final FlatConverter flatConverter;

    @Value("${olx.base-url}")
    private String baseUrl;

    @Value("${olx.flats-path}")
    private String flatsPath;

    public void parseFlats() {
        int minPrice = 250;
        int maxPrice = 900;

        for (int rooms = 1; rooms <= 3; rooms++) {
            for (int price = minPrice; price <= maxPrice; price += 50) {
                for (int pn = 1; pn <= 25; pn++) {
                    List<FlatDto> flats = getFlats(price, price + 50, rooms, pn);
                    flats.stream()
                            .map(flatConverter::toEntity)
                            .forEach(e -> flatRepository.findByName(e.getName())
                                    .ifPresentOrElse(ent -> {
                                        ent.setCost(e.getCost());
                                        flatRepository.save(ent);
                                    }, () -> flatRepository.save(e)));
                }
            }
        }
    }

    public List<FlatDto> getFlats(int minPrice, int maxPrice, int roomCount, int pageNum) {
        Map<String, Integer> params = Map.of("minPrice", minPrice, "maxPrice", maxPrice, "roomCount", roomCount,
                "pageNumber", pageNum);
        String rawHtml = restTemplate.getForObject(flatsPath, String.class, params);

        Document document = Jsoup.parse(rawHtml);
        Elements root = document.getElementsByClass("offer-wrapper");

        return root
                .stream()
                .filter(e -> e.getElementsByAttribute("href").size() == 3)
                .map(this::parseFlatElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(d -> d.setRoomNumber(roomCount))
                .toList();
    }

    private Optional<FlatDto> parseFlatElement(Element flatElement) {
        try {
            FlatDtoBuilder builder = FlatDto.builder();

            builder.link(flatElement.getElementsByAttribute("href").get(1).attr("href"));

            String text = flatElement.text()
                    .replace(" Договорная", "");
//            log.info("text1" + text);

            if (text.contains("000 сум")) {
                return Optional.empty();
            }

            String nameAndCost = text.substring(0, text.indexOf("у.е. Т")).trim();
//            log.info("nameAndCost" + nameAndCost);

            text = text
                    .replace(" Квартиры » Аренда долгосрочная", "")
                    .replace("Ташкент,", "")
                    .trim();
//            log.info("text2" + text);

            nameAndCost = nameAndCost.replace(" Квартиры » Аренда долгосрочная", "");
            int yeIndex = text.indexOf("у.е.");

            int whLastIndex = nameAndCost.lastIndexOf(' ');
            String name = nameAndCost.substring(0, whLastIndex).trim();
            String cost = nameAndCost.substring(whLastIndex + 1).trim();
            builder.name(name);
            builder.cost(Integer.parseInt(cost));

            int districtIndex = text.lastIndexOf(" район ");
//            log.info("districtIndex" + districtIndex);
            String district = text.substring(yeIndex + 4, districtIndex).trim();
            district = district.replaceAll(".* ", "");
            builder.district(district);

            if (text.contains("Сегодня")) {
                builder.lastUpdatedAt(LocalDate.now());
            } else {
                String date = text.substring(districtIndex + 7).trim();
                builder.lastUpdatedAt(getDate(date));
            }

            return Optional.of(builder.build());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return Optional.empty();
    }

    private LocalDate getDate(String rawStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (rawStr.contains(" декабря ")) {
            return LocalDate.parse(rawStr.replace(" декабря ", ".12."), formatter);
        }
        if (rawStr.contains(" ноября ")) {
            return LocalDate.parse(rawStr.replace(" ноября ", ".11."), formatter);
        }
        if (rawStr.contains(" окт.")) {
            String replace = rawStr.replace(" окт.", ".10.2022");
            if (replace.length() == 9) {
                replace = "0" + replace;
            }
            return LocalDate.parse(replace, formatter);
        }
        if (rawStr.contains(" сен.")) {
            String replace = rawStr.replace(" сен.", ".09.2022");
            if (replace.length() == 9) {
                replace = "0" + replace;
            }
            return LocalDate.parse(replace, formatter);
        }
        if (rawStr.contains(" августа ")) {
            return LocalDate.parse(rawStr.replace(" августа ", ".08."), formatter);
        }
        if (rawStr.contains(" июля ")) {
            return LocalDate.parse(rawStr.replace(" июля ", ".07."), formatter);
        }
        if (rawStr.contains(" июня ")) {
            return LocalDate.parse(rawStr.replace(" июня ", ".06."), formatter);
        }
        if (rawStr.contains(" мая ")) {
            return LocalDate.parse(rawStr.replace(" мая ", ".05."), formatter);
        }
        if (rawStr.contains(" апреля ")) {
            return LocalDate.parse(rawStr.replace(" апреля ", ".04."), formatter);
        }
        if (rawStr.contains(" марта ")) {
            return LocalDate.parse(rawStr.replace(" марта ", ".03."), formatter);
        }
        if (rawStr.contains(" февраля ")) {
            return LocalDate.parse(rawStr.replace(" февраля ", ".02."), formatter);
        }
        if (rawStr.contains(" января ")) {
            return LocalDate.parse(rawStr.replace(" января ", ".01."), formatter);
        }
        return LocalDate.now();
    }

}
