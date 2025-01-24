package com.campusland.springbootdemo.infrastructure.repository.topic;

import com.campusland.springbootdemo.application.service.TopicService;
import com.campusland.springbootdemo.domain.dto.topic.TopicRequest;
import com.campusland.springbootdemo.domain.dto.topic.TopicResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.entity.Topic;
import com.campusland.springbootdemo.domain.mapper.TopicMapper;
import com.campusland.springbootdemo.infrastructure.repository.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final TopicMapper topicMapper;


    public TopicServiceImpl(TopicRepository topicRepository, CategoryRepository categoryRepository) {
        this.topicRepository = topicRepository;
        this.categoryRepository = categoryRepository;
        this.topicMapper = new TopicMapper();
    }

    @Override
    public Page<TopicResponse> finAll(Pageable pageable) {
        return topicRepository.findAll(pageable).map(topicMapper::toResponse);
    }

    @Override
    public List<TopicResponse> findAllBySearch(String name, Pageable pageable) {
        return topicRepository.findByTitleContainsIgnoreCase(name, pageable).stream().map(topicMapper::toResponse).toList();
    }

    @Override
    public Page<TopicResponse> findAllByCategoryId(Pageable pageable, Long id) {
        return topicRepository.findByCategory_Id(pageable, id).map(topicMapper::toResponse);
    }

    @Override
    public Optional<TopicResponse> findById(Long id) {
        return topicRepository.findById(id).map(topicMapper::toResponse);
    }

    @Override
    public TopicResponse create(TopicRequest topic) {
        var category = findCategoryById(topic.getCategoryId());
        return topicMapper.toResponse(
                topicRepository.save(
                        topicMapper.toEntity(topic, new Category[]{category})
                ));
    }

    @Override
    public TopicResponse update(Long id, TopicRequest topic) {
        var response = findTopicById(id);
        var category = findCategoryById(topic.getCategoryId());
        return topicMapper.toResponse(
                topicRepository.save(
                        topicMapper.toUpdateEntity(topic, response, new Category[]{category})
                )
        );
    }

    @Override
    public TopicResponse delete(Long id) {
        var response = findTopicById(id);
        topicRepository.delete(response);
        return topicMapper.toResponse(response);
    }

    /**
     * Busca un tema (`Topic`) en el repositorio por su ID.
     * Si el tema no es encontrado, lanza una excepción con un mensaje de error.
     *
     * @param id El ID del tema a buscar.
     * @return El objeto `Topic` correspondiente al ID proporcionado.
     * @throws RuntimeException Si no se encuentra un tema con el ID proporcionado.
     */
    private Topic findTopicById(Long id) {
        var response = topicRepository.findById(id);
        if (response.isEmpty()) {
            throw new RuntimeException("Topic not found by id " + id);
        }
        return response.get();
    }

    /**
     * Busca una categoría (`Category`) en el repositorio por su ID.
     * Si la categoría no es encontrada, lanza una excepción con un mensaje de error.
     *
     * @param id El ID de la categoría a buscar.
     * @return El objeto `Category` correspondiente al ID proporcionado.
     * @throws RuntimeException Si no se encuentra una categoría con el ID proporcionado.
     */
    private Category findCategoryById(Long id) {
        var response = categoryRepository.findById(id);
        if (response.isEmpty()) {
            throw new RuntimeException("Category not found by id " + id);
        }
        return response.get();
    }
}
