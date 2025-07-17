package com.example.prak.service;

import com.example.prak.repository.CategoryRepository;
import com.example.prak.repository.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> getCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
