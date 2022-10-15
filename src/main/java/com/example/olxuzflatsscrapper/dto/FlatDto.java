package com.example.olxuzflatsscrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlatDto {

    private UUID id;
    private String name;
    private int roomNumber;
    private int cost;
    private String district;
    private String link;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDate lastUpdatedAt;
}
