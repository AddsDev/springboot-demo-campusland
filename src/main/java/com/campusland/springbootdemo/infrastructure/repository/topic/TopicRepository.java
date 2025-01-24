package com.campusland.springbootdemo.infrastructure.repository.topic;

import com.campusland.springbootdemo.domain.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findByCategory_Id(Pageable pageable, Long categoryId);
    List<Topic> findByTitleContainsIgnoreCase(String title, Pageable pageable);
}
