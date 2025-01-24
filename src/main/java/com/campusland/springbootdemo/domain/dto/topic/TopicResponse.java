package com.campusland.springbootdemo.domain.dto.topic;

import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TopicResponse implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Double rating;
    private Integer views;
    private CategoryResponse category;
    private String createdAt;
}

