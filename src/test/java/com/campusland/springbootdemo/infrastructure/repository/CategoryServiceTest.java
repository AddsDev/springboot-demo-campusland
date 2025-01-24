package com.campusland.springbootdemo.infrastructure.repository;

import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.mapper.CategoryMapper;
import com.campusland.springbootdemo.infrastructure.error.CategoryNotFoundException;
import com.campusland.springbootdemo.infrastructure.repository.category.CategoryRepository;
import com.campusland.springbootdemo.infrastructure.repository.category.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category mockCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setCreatedAt(Instant.now());
        return category;
    }

    private CategoryResponse mockCategoryResponse() {
        var response = new CategoryResponse();
        response.setId(1L);
        response.setName("Test Category");
        response.setCreatedAt("2024-02-15 10:34:21");
        return response;
    }

    private CategoryRequest mockCategoryRequest() {
        var request = new CategoryRequest();
        request.setName("field");
        return request;
    }

    @Test
    void findAll_shouldReturnPagedCategoryResponses() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(mockCategory());
        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<CategoryResponse> result = categoryService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    void findBySearch_shouldReturnFilteredCategories() {
        String name = "test";
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(mockCategory());

        when(categoryRepository.findByNameContains(name, pageable)).thenReturn(categories);

        List<CategoryResponse> result = categoryService.findBySearch(name, pageable);

        assertEquals(1, result.size());
        verify(categoryRepository).findByNameContains(name, pageable);
    }

    @Test
    void findById_shouldReturnCategoryResponseWhenFound() {
        Long id = 1L;
        Category category = mockCategory();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<CategoryResponse> result = categoryService.findById(id);

        assertTrue(result.isPresent());
        verify(categoryRepository).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenNotFound() {

        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());


        Optional<CategoryResponse> result = categoryService.findById(id);


        assertTrue(result.isEmpty());
        verify(categoryRepository).findById(id);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void crate_shouldSaveCategoryAndReturnResponse() {
        CategoryRequest request = mockCategoryRequest();
        Category category = mockCategory();
        CategoryResponse response = mockCategoryResponse();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse result = categoryService.crate(request);

        assertEquals(response.getId(), result.getId());
    }

    @Test
    void update_shouldUpdateAndReturnCategoryResponse() {
        Long id = 1L;
        CategoryRequest request = mockCategoryRequest();
        Category existingCategory = mockCategory();
        Category updatedCategory = mockCategory();
        CategoryResponse response = mockCategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryResponse result = categoryService.update(request, id);

        assertEquals(response.getId(), result.getId());
        verify(categoryRepository).findById(id);
    }

    @Test
    void delete_shouldDeleteCategoryAndReturnResponse() {
        Long id = 1L;
        Category category = mockCategory();
        CategoryResponse response = mockCategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponse result = categoryService.delete(id);

        assertEquals(response.getId(), result.getId());
        verify(categoryRepository).findById(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrowExceptionWhenCategoryNotFound() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(id));
        verify(categoryRepository).findById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

}
