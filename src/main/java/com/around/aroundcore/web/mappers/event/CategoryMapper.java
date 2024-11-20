package com.around.aroundcore.web.mappers.event;

import com.around.aroundcore.database.models.event.Category;
import com.around.aroundcore.web.dtos.events.timepad.TimepadCategory;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryMapper implements Function<TimepadCategory, Category> {
    @Override
    public Category apply(TimepadCategory timepadCategory) {
        return Category.builder()
                .id(timepadCategory.getId())
                .name(timepadCategory.getName())
                .build();
    }
}
