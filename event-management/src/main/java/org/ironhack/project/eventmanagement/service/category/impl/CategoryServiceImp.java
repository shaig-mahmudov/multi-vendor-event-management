package org.ironhack.project.eventmanagement.service.category.impl;

import org.ironhack.project.eventmanagement.dto.request.category.CategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.CategoryResponse;
import org.ironhack.project.eventmanagement.entity.Category;
import org.ironhack.project.eventmanagement.exception.ConflictException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.mapper.CategoryMapper;
import org.ironhack.project.eventmanagement.repository.CategoryRepository;
import org.ironhack.project.eventmanagement.service.category.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    CategoryServiceImp(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    // create
    @Override
    public CategoryResponse create(CategoryRequest request){
        String name = request.getName().trim();
        if(repository.existsByNameIgnoreCase(name)){
            throw new ConflictException("Category already exists: " + name);
        }

        Category category = mapper.toEntity(request);
        category.setName(name);

        Category saved  = repository.save(category);

        return mapper.toResponse(saved);
    }

    // get all
    @Override
    public List<CategoryResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // get by id
    @Override
    public CategoryResponse getById(Long id){
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return mapper.toResponse(category);
    }

    // update
    @Override
    public CategoryResponse update(Long id, CategoryRequest request){
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        String newName = request.getName().trim();

        if(!category.getName().equalsIgnoreCase(newName) && repository.existsByNameIgnoreCase(newName)){
            throw new ConflictException("Category already exists: " + request.getName());
        }

        mapper.updateEntity(request,category);
        category.setName(newName);

        Category updated = repository.save(category);
        return mapper.toResponse(updated);
    }

    // delete
    @Override
    public void delete(Long id){
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        // if category have events can't delete it
        if(category.getEvents() != null && !category.getEvents().isEmpty()){
            throw new ConflictException("Cannot delete category with events");
        }
        repository.delete(category);
    }
}
