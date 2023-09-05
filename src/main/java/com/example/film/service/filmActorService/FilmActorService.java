package com.example.film.service.filmActorService;


import com.example.film.repository.FilmActorRepository;
import com.example.film.repository.FilmCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class FilmActorService {
    private final FilmActorRepository filmActorRepository;

    public void deleteByFilmId(Long filmId) {
        filmActorRepository.deleteFilmActorsByFilmId(filmId);
    }
}
