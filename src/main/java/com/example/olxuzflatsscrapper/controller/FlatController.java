package com.example.olxuzflatsscrapper.controller;

import com.example.olxuzflatsscrapper.dto.FlatDto;
import com.example.olxuzflatsscrapper.scraper.FlatScraper;
import com.example.olxuzflatsscrapper.service.FlatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlatController {

    private final FlatService flatService;
    private final FlatScraper flatScraper;

    @GetMapping
    public List<FlatDto> getFlats() {
        flatScraper.parseFlats();
        return List.of();
//        return flatScraper.getFlats(350, 390, 1);
    }

    @PostMapping
    public FlatDto save(@RequestBody FlatDto flatDto) {
        return flatService.save(flatDto);
    }

}
