package com.campusland.springbootdemo.infrastructure.repository;

import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.entity.Topic;
import com.campusland.springbootdemo.infrastructure.repository.category.CategoryRepository;
import com.campusland.springbootdemo.infrastructure.repository.topic.TopicRepository;
import com.campusland.springbootdemo.infrastructure.repository.topic.TopicServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    private static final Long TOPIC_ID = 1L;
    private static final Long CATEGORY_ID = 1L;

    private Topic mockTopic() {
        Topic topic = new Topic();
        topic.setId(TOPIC_ID);
        topic.setTitle("Test Topic");
        topic.setDescription("Test Description");
        topic.setRating(4.0);
        topic.setViews(100);
        topic.setCreatedAt(Instant.now());
        topic.setUpdatedAt(Instant.now());
        topic.setCategory(mockCategory());
        return topic;
    }

    private Category mockCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setName("Test Category");
        category.setCreatedAt(Instant.now());
        category.setUpdateAt(Instant.now());
        return category;
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
    void findAll_shouldReturnAllTopics() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topic> topicPage = new PageImpl<>(List.of(mockTopic()));
        when(topicRepository.findAll(pageable)).thenReturn(topicPage);

        Page<TopicResponse> result = topicService.finAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(TOPIC_ID, result.getContent().get(0).getId());
        verify(topicRepository).findAll(pageable);
    }

    @Test
    void findAllBySearch_shouldReturnTopicsMatchingSearch() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Topic> topicList = List.of(mockTopic());
        when(topicRepository.findByTitleContainsIgnoreCase("Test", pageable)).thenReturn(topicList);
        
        List<TopicResponse> result = topicService.findAllBySearch("Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TOPIC_ID, result.get(0).getId());
        verify(topicRepository).findByTitleContainsIgnoreCase("Test", pageable);
    }

    @Test
    void findById_shouldReturnTopicResponseWhenTopicExists() {
        when(topicRepository.findById(TOPIC_ID)).thenReturn(Optional.of(mockTopic()));

        Optional<TopicResponse> result = topicService.findById(TOPIC_ID);

        assertTrue(result.isPresent());
        assertEquals(TOPIC_ID, result.get().getId());
        verify(topicRepository).findById(TOPIC_ID);
    }

    @Test
    void create_shouldReturnCreatedTopicResponse() {
        TopicRequest request = mockTopicRequest();
        Category category = mockCategory();
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(topicRepository.save(any(Topic.class))).thenReturn(mockTopic());
        
        TopicResponse result = topicService.create(request);

        assertNotNull(result);
        assertEquals(TOPIC_ID, result.getId());
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void update_shouldReturnUpdatedTopicResponse() {
        TopicRequest request = mockTopicRequest();
        when(topicRepository.findById(TOPIC_ID)).thenReturn(Optional.of(mockTopic()));
        Category category = mockCategory();
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(topicRepository.save(any(Topic.class))).thenReturn(mockTopic());
        
        TopicResponse result = topicService.update(TOPIC_ID, request);

        assertNotNull(result);
        assertEquals(TOPIC_ID, result.getId());
        verify(topicRepository).findById(TOPIC_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void delete_shouldReturnDeletedTopicResponse() {
        Topic topic = mockTopic();
        when(topicRepository.findById(TOPIC_ID)).thenReturn(Optional.of(topic));

        
        TopicResponse result = topicService.delete(TOPIC_ID);

        assertNotNull(result);
        assertEquals(TOPIC_ID, result.getId());
        verify(topicRepository).delete(topic);
    }

    @Test
    void delete_shouldThrowExceptionWhenTopicNotFound() {
        when(topicRepository.findById(TOPIC_ID)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> topicService.delete(TOPIC_ID));
    }
}

