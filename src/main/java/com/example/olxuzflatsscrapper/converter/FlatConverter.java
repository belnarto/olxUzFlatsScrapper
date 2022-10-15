package com.example.olxuzflatsscrapper.converter;

import com.example.olxuzflatsscrapper.dto.FlatDto;
import com.example.olxuzflatsscrapper.entity.FlatEntity;
import org.springframework.stereotype.Component;

@Component
public class FlatConverter {

    public FlatDto toDto(FlatEntity flatEntity) {
        return FlatDto.builder()
            .id(flatEntity.getId())
            .roomNumber(flatEntity.getRoomNumber())
            .cost(flatEntity.getCost())
            .link(flatEntity.getLink())
            .district(flatEntity.getDistrict())
            .name(flatEntity.getName())
            .lastUpdatedAt(flatEntity.getLastUpdatedAt())
            .build();
    }

    public FlatEntity toEntity(FlatDto flatDto) {
        return FlatEntity.builder()
            .id(flatDto.getId())
            .roomNumber(flatDto.getRoomNumber())
            .cost(flatDto.getCost())
            .link(flatDto.getLink())
            .district(flatDto.getDistrict())
            .name(flatDto.getName())
            .lastUpdatedAt(flatDto.getLastUpdatedAt())
            .build();
    }

}
