package com.around.aroundcore.database.repositories.event;

import com.around.aroundcore.database.models.event.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}