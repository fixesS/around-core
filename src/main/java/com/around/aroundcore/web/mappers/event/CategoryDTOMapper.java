package com.around.aroundcore.web.mappers.event;

import com.around.aroundcore.database.models.event.Category;
import com.around.aroundcore.web.dtos.events.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryDTOMapper  implements Function<Category, CategoryDTO> {
    @Override
    public CategoryDTO apply(Category category) {
        return CategoryDTO.builder().id(category.getId()).name(category.getName()).build();
    }
}
