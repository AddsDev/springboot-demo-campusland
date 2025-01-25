package com.campusland.springbootdemo.domain.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryRequest implements Serializable {
    @NotBlank(message = "field is mandatory")
    @Size(min = 1, max = 50)
    private String name;
}