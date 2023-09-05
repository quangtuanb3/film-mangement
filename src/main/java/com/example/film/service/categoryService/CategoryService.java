package com.example.film.service.categoryService;

import com.example.film.domain.*;
import com.example.film.repository.CategoryRepository;
import com.example.film.repository.FilmActorRepository;
import com.example.film.repository.FilmCategoryRepository;
import com.example.film.repository.FilmRepository;
import com.example.film.service.categoryService.request.FilmSaveRequest;
import com.example.film.service.categoryService.response.CategoryListResponse;
import com.example.film.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryListResponse
                        .builder()
                        .value(category.getId())
                        .name(category.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
    }
}
