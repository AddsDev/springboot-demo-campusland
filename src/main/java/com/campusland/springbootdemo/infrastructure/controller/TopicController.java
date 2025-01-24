package com.campusland.springbootdemo.infrastructure.controller;


import com.campusland.springbootdemo.application.service.TopicService;
import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import com.campusland.springbootdemo.infrastructure.util.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/topic", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public Page<TopicResponse> findAllTopics(
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "title", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "views", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC),
            }) Pageable pageable
    ) {
        return topicService.finAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> findById(@PathVariable @Positive Long id) {
        return topicService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{id}")
    public Page<TopicResponse> findByCategoryId(
            @PathVariable @Positive Long id,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "title", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "views", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC),
            }) Pageable pageable) {
        return topicService.findAllByCategoryId(pageable, id);
    }

    @GetMapping("/search")
    public Page<TopicResponse> findCategoryByName(
            @RequestParam @NotBlank String title,
            @PageableDefault(size = Pagination.DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "title", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "views", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC),
            }) Pageable pageable
    ) {
        var response = topicService.findAllBySearch(title, pageable);
        return Pagination.customPage(pageable, response);
    }

    @PostMapping
    public ResponseEntity<TopicResponse> create(@RequestBody @Valid TopicRequest topicRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.create(topicRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponse> update(@RequestBody @Valid TopicRequest topicRequest, @PathVariable @Positive Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.update(id, topicRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TopicResponse> delete(@PathVariable @Positive Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.delete(id));
    }

}
