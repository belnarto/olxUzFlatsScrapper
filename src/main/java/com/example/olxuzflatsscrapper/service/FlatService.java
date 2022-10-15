package com.example.olxuzflatsscrapper.service;

import com.example.olxuzflatsscrapper.converter.FlatConverter;
import com.example.olxuzflatsscrapper.dto.FlatDto;
import com.example.olxuzflatsscrapper.repository.FlatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlatService {

    private final FlatRepository flatRepository;
    private final FlatConverter flatConverter;

    public List<FlatDto> findAll() {
        return flatRepository.findAll().stream()
            .map(flatConverter::toDto)
            .toList();
    }

    public FlatDto save(FlatDto flatDto) {
        return flatConverter.toDto(flatRepository.save(flatConverter.toEntity(flatDto)));
    }

}
