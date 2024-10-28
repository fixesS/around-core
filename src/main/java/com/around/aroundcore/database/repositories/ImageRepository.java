package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByFile(String file);
    void deleteByFile(String file);
    void deleteByUuid(UUID uuid);
}
