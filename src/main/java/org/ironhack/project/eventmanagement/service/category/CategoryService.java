package org.ironhack.project.eventmanagement.service.category;

import org.ironhack.project.eventmanagement.dto.request.category.CategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    CategoryResponse update(Long id,CategoryRequest request);
    void delete(Long id);
}
