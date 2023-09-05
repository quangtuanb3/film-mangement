package com.example.film.controller.rest;

import com.example.film.domain.Film;
import com.example.film.repository.FilmRepository;
import com.example.film.service.filmService.FilmService;
import com.example.film.service.filmService.request.FilmSaveRequest;
import com.example.film.service.filmService.response.FilmEditResponse;
import com.example.film.service.filmService.response.FilmListResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/films")
@AllArgsConstructor
public class FilmRestController {

    private final FilmService filmService;
    private final FilmRepository filmRepository;


    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid FilmSaveRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(-1L, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(filmService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<FilmListResponse>> list(@PageableDefault(size = 5) Pageable pageable,
                                                       @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(filmService.getAll(pageable, search), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmEditResponse> getFilmById(@PathVariable Long id) {
        FilmEditResponse film = filmService.findFilmToEditById(id);
        if (film != null) {
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateFilm(@PathVariable Long id, @RequestBody FilmSaveRequest updatedFilm) {
        boolean isUpdatedFilm = filmService.updateFilm(id, updatedFilm);
        if (isUpdatedFilm) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteFilm(@PathVariable Long id) {
        boolean isDeletedFilm = filmService.deleteFilm(id);
        if (isDeletedFilm) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
