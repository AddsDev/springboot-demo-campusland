package com.campusland.springbootdemo.infrastructure.repository.category;

import com.campusland.springbootdemo.domain.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContains(String name, Pageable pageable);
}
