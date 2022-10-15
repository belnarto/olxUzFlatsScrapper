package com.example.olxuzflatsscrapper.controller;

import com.example.olxuzflatsscrapper.dto.FlatDto;
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

    @GetMapping
    public List<FlatDto> getAll() {
        return flatService.findAll();
    }

    @PostMapping
    public FlatDto save(@RequestBody FlatDto flatDto) {
        return flatService.save(flatDto);
    }

}
