package com.campusland.springbootdemo.application.service;

import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Page<TopicResponse> finAll(Pageable pageable);
    List<TopicResponse> findAllBySearch(String name, Pageable pageable);
    Page<TopicResponse> findAllByCategoryId(Pageable pageable, Long id);
    Optional<TopicResponse> findById(Long id);
    TopicResponse create(TopicRequest topic);
    TopicResponse update(Long id, TopicRequest topic);
    TopicResponse delete(Long id);
}
