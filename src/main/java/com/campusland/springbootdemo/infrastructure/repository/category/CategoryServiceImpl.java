package com.campusland.springbootdemo.infrastructure.repository.category;

import com.campusland.springbootdemo.application.service.CategoryService;
import com.campusland.springbootdemo.domain.dto.category.CategoryRequest;
import com.campusland.springbootdemo.domain.dto.category.CategoryResponse;
import com.campusland.springbootdemo.domain.entity.Category;
import com.campusland.springbootdemo.domain.mapper.CategoryMapper;
import com.campusland.springbootdemo.infrastructure.error.CategoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = new CategoryMapper();
    }
    @Override
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponse);
    }

    @Override
    public List<CategoryResponse> findBySearch(String name, Pageable pageable) {
        return categoryRepository.findByNameContains(name, pageable).stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public Optional<CategoryResponse> findById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toResponse);
    }

    @Override
    public CategoryResponse crate(CategoryRequest categoryRequest) {
        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toEntity(categoryRequest)));
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, Long id) {
        var response = getCategoryById(id);

        return categoryMapper.toResponse(
                categoryRepository.save(
                        categoryMapper.toUpdateEntity(categoryRequest, response)
                )
        );
    }

    @Override
    public CategoryResponse delete(Long id) {
        var response = getCategoryById(id);
        categoryRepository.deleteById(id);
        return categoryMapper.toResponse(response);
    }

    /**
     * Busca una categoría (`Category`) en el repositorio por su ID.
     * Si la categoría no es encontrada, lanza una excepción con un mensaje de error.
     *
     * @param id El ID de la categoría a buscar.
     * @return El objeto `Category` correspondiente al ID proporcionado.
     * @throws RuntimeException Si no se encuentra una categoría con el ID proporcionado.
     */
    private Category getCategoryById(Long id) {
        var response = categoryRepository.findById(id);
        if (response.isEmpty()) {
            throw new CategoryNotFoundException("Category not found by id " + id);
        }
        return response.get();
    }
}
