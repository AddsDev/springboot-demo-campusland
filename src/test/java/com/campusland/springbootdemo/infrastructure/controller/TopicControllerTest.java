package com.campusland.springbootdemo.infrastructure.controller;

import com.campusland.springbootdemo.application.service.TopicService;
import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import com.campusland.springbootdemo.domain.entity.Topic;
import com.campusland.springbootdemo.domain.util.FormatInstant;
import com.campusland.springbootdemo.infrastructure.error.CategoryNotFoundException;
import com.campusland.springbootdemo.infrastructure.error.TopicNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TopicService topicService;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String BASE_URL = "/topic";

    private static final Long TOPIC_ID = 1L;
    private static final Long CATEGORY_ID = 1L;

    private CategoryResponse mockCategoryResponse() {
        var response = new CategoryResponse();
        response.setId(1L);
        response.setName("Test Category");
        response.setCreatedAt(FormatInstant.formatInstant(Instant.now()));
        return response;
    }

    private TopicResponse mockTopicResponse() {
        TopicResponse response = new TopicResponse();
        response.setId(TOPIC_ID);
        response.setTitle("Test Topic");
        response.setDescription("Test Description");
        response.setRating(4.0);
        response.setViews(100);
        response.setCategory(mockCategoryResponse());
        response.setCreatedAt("2024-02-15 10:34:21");
        return response;
    }

    private TopicRequest mockTopicRequest() {
        TopicRequest request = new TopicRequest();
        request.setName("Test Topic");
        request.setDescription("Test Description");
        request.setRating(4.0);
        request.setViews(100);
        request.setCategoryId(CATEGORY_ID);
        return request;
    }

    @Test
    void findAllTopics_shouldReturnTopicsPage() throws Exception {
        Page<TopicResponse> pageResponse = new PageImpl<>(List.of(mockTopicResponse()));
        when(topicService.finAll(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(TOPIC_ID))
                .andExpect(jsonPath("$.content[0].title").value("Test Topic"));

        verify(topicService).finAll(any(Pageable.class));
    }

    @Test
    void findAllTopics_shouldReturnEmptyPageWhenNoResults() throws Exception {
        when(topicService.finAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(topicService).finAll(any(Pageable.class));
    }

    @Test
    void findById_shouldReturnTopic_whenTopicIfExists() throws Exception {
        when(topicService.findById(TOPIC_ID)).thenReturn(Optional.of(mockTopicResponse()));

        mockMvc.perform(get(BASE_URL + "/{id}", TOPIC_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TOPIC_ID))
                .andExpect(jsonPath("$.title").value("Test Topic"));
    }

    @Test
    void findById_shouldReturnNotFound_whenTopicDoesNotExist() throws Exception {
        when(topicService.findById(TOPIC_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/{id}", TOPIC_ID))
                .andExpect(status().isNotFound());

        verify(topicService).findById(TOPIC_ID);
    }

    @Test
    void findCategoryById_shouldReturnBadRequestIfIdIsInvalid() throws Exception {
        mockMvc.perform(get(BASE_URL + "/-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(topicService);
    }

    @Test
    void findByCategoryId_shouldReturnTopicsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TopicResponse> pageResponse = new PageImpl<>(List.of(mockTopicResponse()));
        when(topicService.findAllByCategoryId(any(Pageable.class), eq(CATEGORY_ID))).thenReturn(pageResponse);

        mockMvc.perform(get(BASE_URL + "/category/{id}", CATEGORY_ID)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(TOPIC_ID))
                .andExpect(jsonPath("$.content[0].title").value("Test Topic"));

        verify(topicService).findAllByCategoryId(any(Pageable.class), eq(CATEGORY_ID));
    }

    @Test
    void findCategoryByName_shouldReturnFilteredTopicsPage() throws Exception {
        String title = "Test";
        List<TopicResponse> responseList = List.of(mockTopicResponse());
        when(topicService.findAllBySearch(eq(title), any(Pageable.class))).thenReturn(responseList);

        mockMvc.perform(get(BASE_URL + "/search")
                        .param("title", title)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(TOPIC_ID))
                .andExpect(jsonPath("$.content[0].title").value("Test Topic"));

        verify(topicService).findAllBySearch(eq(title), any(Pageable.class));
    }

    @Test
    void create_shouldReturnCreatedTopicSuccessfully() throws Exception {
        TopicRequest request = mockTopicRequest();
        when(topicService.create(any(TopicRequest.class))).thenReturn(mockTopicResponse());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TOPIC_ID))
                .andExpect(jsonPath("$.title").value("Test Topic"));

        verify(topicService).create(any(TopicRequest.class));
    }

    @Test
    void create_shouldReturnBadRequestForInvalidRequest() throws Exception {
        TopicRequest invalidRequest = new TopicRequest();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("views"))
                .andExpect(jsonPath("$[0].description").value("must not be null"));

        verifyNoInteractions(topicService);
    }

    @Test
    void update_shouldReturnUpdatedTopicSuccessfully() throws Exception {
        TopicRequest request = mockTopicRequest();
        when(topicService.update(eq(TOPIC_ID), any(TopicRequest.class))).thenReturn(mockTopicResponse());

        mockMvc.perform(put(BASE_URL + "/{id}", TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TOPIC_ID))
                .andExpect(jsonPath("$.title").value("Test Topic"));
        verify(topicService).update(eq(TOPIC_ID), any(TopicRequest.class));
    }

    @Test
    void update_shouldReturnBadRequestForInvalidRequest() throws Exception {
        TopicRequest invalidRequest = new TopicRequest();

        mockMvc.perform(put(BASE_URL + "/{id}", TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(topicService);
    }

    @Test
    void update_shouldReturnNotFoundIfCategoryDoesNotExist() throws Exception {
        when(topicService.update(eq(TOPIC_ID), any(TopicRequest.class)))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(put(BASE_URL + "/{id}", TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mockTopicRequest())))
                .andExpect(status().isNotFound());
        verify(topicService).update(eq(TOPIC_ID), any(TopicRequest.class));
    }

    @Test
    void update_shouldReturnNotFoundIfTopicDoesNotExist() throws Exception {
        when(topicService.update(eq(TOPIC_ID), any(TopicRequest.class)))
                .thenThrow(new TopicNotFoundException("Topic not found"));

        mockMvc.perform(put(BASE_URL + "/{id}", TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mockTopicRequest())))
                .andExpect(status().isNotFound());
        verify(topicService).update(eq(TOPIC_ID), any(TopicRequest.class));
    }

    @Test
    void delete_shouldReturnDeletedTopicSuccessfully() throws Exception {
        when(topicService.delete(TOPIC_ID)).thenReturn(mockTopicResponse());

        mockMvc.perform(delete(BASE_URL + "/{id}", TOPIC_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TOPIC_ID))
                .andExpect(jsonPath("$.title").value("Test Topic"));

        verify(topicService).delete(TOPIC_ID);
    }

    @Test
    void delete_shouldReturnNotFoundIfTopicDoesExist() throws Exception {
        doThrow(new TopicNotFoundException("Topic not found")).when(topicService).delete(TOPIC_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", TOPIC_ID))
                .andExpect(status().isNotFound());
    }
}
