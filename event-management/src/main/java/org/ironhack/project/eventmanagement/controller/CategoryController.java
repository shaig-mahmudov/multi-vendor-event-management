package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironhack.project.eventmanagement.dto.request.category.CategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.CategoryResponse;
import org.ironhack.project.eventmanagement.service.category.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request){
        return categoryService.create(request);
    }

    @GetMapping
    public List<CategoryResponse> getAll(){
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id){
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }
}
