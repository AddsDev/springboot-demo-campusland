package com.campusland.springbootdemo.infrastructure.controller;

import com.campusland.springbootdemo.application.service.CategoryService;
import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.util.FormatInstant;
import com.campusland.springbootdemo.infrastructure.error.CategoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String BASE_URL = "/category";

    private CategoryResponse mockCategoryResponse() {
        var response = new CategoryResponse();
        response.setId(1L);
        response.setName("Test Category");
        response.setCreatedAt(FormatInstant.formatInstant(Instant.now()));
        return response;
    }

    private CategoryRequest mockCategoryRequest() {
        var request = new CategoryRequest();
        request.setName("field");
        return request;
    }

    @Test
    void findAllCategories_shouldReturnCategoriesPage() throws Exception {
        Page<CategoryResponse> categoryPage = new PageImpl<>(List.of(mockCategoryResponse()));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryPage);

        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Category"));

        verify(categoryService).findAll(any(Pageable.class));
    }

    @Test
    void findAllCategories_shouldReturnEmptyPageWhenNoResults() throws Exception {
        when(categoryService.findAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(categoryService).findAll(any(Pageable.class));
    }

    @Test
    void findCategoryById_shouldReturnCategoryIfExists() throws Exception {
        when(categoryService.findById(1L)).thenReturn(Optional.of(mockCategoryResponse()));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService).findById(1L);
    }

    @Test
    void findCategoryById_shouldReturnNotFoundIfCategoryDoesNotExist() throws Exception {
        when(categoryService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound());

        verify(categoryService).findById(1L);
    }

    @Test
    void findCategoryById_shouldReturnBadRequestIfIdIsInvalid() throws Exception {
        mockMvc.perform(get(BASE_URL + "/-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    void createCategory_shouldCreateCategorySuccessfully() throws Exception {
        CategoryRequest request = mockCategoryRequest();
        CategoryResponse response = mockCategoryResponse();

        when(categoryService.crate(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService).crate(any(CategoryRequest.class));
    }

    @Test
    void createCategory_shouldReturnBadRequestForInvalidRequest() throws Exception {
        CategoryRequest invalidRequest = new CategoryRequest(); // Campos vacíos

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("name"))
                .andExpect(jsonPath("$[0].description").value("field is mandatory"));

        verifyNoInteractions(categoryService);
    }

    @Test
    void updateCategory_shouldUpdateCategorySuccessfully() throws Exception {
        CategoryRequest request = mockCategoryRequest();
        CategoryResponse response = mockCategoryResponse();

        when(categoryService.update(any(CategoryRequest.class), eq(1L))).thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService).update(any(CategoryRequest.class), eq(1L));
    }

    @Test
    void updateCategory_shouldReturnBadRequestForInvalidRequest() throws Exception {
        CategoryRequest invalidRequest = new CategoryRequest(); // Campos vacíos

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }

    @Test
    void updateCategory_shouldReturnNotFoundIfCategoryDoesNotExist() throws Exception {
        when(categoryService.update(any(CategoryRequest.class), eq(1L)))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mockCategoryRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_shouldDeleteCategorySuccessfully() throws Exception {
        when(categoryService.delete(1L)).thenReturn(mockCategoryResponse());

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());

        verify(categoryService).delete(1L);
    }

    @Test
    void deleteCategory_shouldReturnNotFoundIfCategoryDoesNotExist() throws Exception {
        doThrow(new CategoryNotFoundException("Category not found")).when(categoryService).delete(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound());
    }

}
