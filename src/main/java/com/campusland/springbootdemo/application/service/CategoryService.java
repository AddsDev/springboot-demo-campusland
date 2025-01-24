package com.campusland.springbootdemo.application.service;

import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Page<CategoryResponse> findAll(Pageable pageable);
    List<CategoryResponse> findBySearch(String name, Pageable pageable);
    Optional<CategoryResponse> findById(Long id);
    CategoryResponse crate(CategoryRequest categoryRequest);
    CategoryResponse update(CategoryRequest categoryRequest, Long id);
    CategoryResponse delete(Long id);
}
