package com.campusland.springbootdemo.domain.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TopicRequest implements Serializable {
    @NotBlank(message = "field is mandatory")
    @Size(min = 1, max = 50)
    private String name;
    @NotBlank(message = "field is mandatory")
    @Size(min = 1, max = 255)
    private String description;
    @Positive
    @NotNull
    private Double rating;
    @Positive
    @NotNull
    private Integer views;
    @Positive
    @NotNull
    private Long categoryId;
}
