package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.request.category.CategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.CategoryResponse;
import org.ironhack.project.eventmanagement.entity.Category;

import java.time.LocalDateTime;

public class CategoryMapper {
    // for create
    public Category toEntity(CategoryRequest request){
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreatedAt(LocalDateTime.now());
        return category;
    }

    // for update
    public void updateEntity(CategoryRequest request, Category category){
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdatedAt(LocalDateTime.now());
    }

    // for response
    public CategoryResponse toResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
