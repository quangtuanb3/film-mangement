package com.example.film.service.filmCategoryService;

import com.example.film.repository.FilmCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class FilmCategoryService {
    private final FilmCategoryRepository filmCategoryRepository;

    public void deleteByFilmId(Long filmId) {
        filmCategoryRepository.deleteFilmCategoriesByFilmId(filmId);
    }
}
