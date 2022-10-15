package com.example.olxuzflatsscrapper.repository;

import com.example.olxuzflatsscrapper.entity.FlatEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlatRepository extends JpaRepository<FlatEntity, UUID> {
}
