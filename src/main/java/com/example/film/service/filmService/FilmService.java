package com.example.film.service.filmService;

import com.example.film.domain.*;
import com.example.film.repository.FilmActorRepository;
import com.example.film.repository.FilmCategoryRepository;
import com.example.film.repository.FilmRepository;
import com.example.film.service.categoryService.CategoryService;
import com.example.film.service.filmActorService.FilmActorService;
import com.example.film.service.filmCategoryService.FilmCategoryService;
import com.example.film.service.filmService.request.FilmSaveRequest;
import com.example.film.service.filmService.response.FilmEditResponse;
import com.example.film.service.filmService.response.FilmListResponse;
import com.example.film.service.personService.PersonService;
import com.example.film.service.request.SelectOptionRequest;
import com.example.film.service.response.SelectOptionResponse;
import com.example.film.util.AppUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmCategoryRepository filmCategoryRepository;
    private final FilmActorRepository filmActorRepository;
    private final FilmCategoryService filmCategoryService;
    private final FilmActorService filmActorService;

    public Long create(FilmSaveRequest request) {
        var film = AppUtil.mapper.map(request, Film.class);
        film = filmRepository.save(film);
        var filmCategories = new ArrayList<FilmCategory>();

        for (String idCategory : request.getCategories()) {
            Category category = new Category(Long.valueOf(idCategory));
            filmCategories.add(new FilmCategory(film, category));
        }
        filmCategoryRepository.saveAll(filmCategories);

        var filmActors = new ArrayList<FilmActor>();
        List<Person> actors = new ArrayList<>();
        for (String idActor : request.getActors()) {
            Person actor = new Person(Long.valueOf(idActor));
            filmActors.add(new FilmActor(actor, film));
        }
        filmActorRepository.saveAll(filmActors);

        return film.getId();
    }

    public Page<FilmListResponse> getAll(Pageable pageable, String search) {
        search = "%" + search + "%";
        return filmRepository.searchEverythingIgnorePublishDate(search, pageable).map(film -> FilmListResponse.builder()
                .id(film.getId())
                .name(film.getName())
                .director(film.getDirector().getName())
                .publishDate(film.getPublishDate())
                .actors(film.getFilmActors()
                        .stream()
                        .map(filmActor -> filmActor.getActor().getName())
                        .collect(Collectors.joining(", ")))
                .categories(film.getFilmCategories()
                        .stream()
                        .map(filmCategory -> filmCategory.getCategory().getName())
                        .collect(Collectors.joining(", "))).build());
    }

    public Film findById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
    }

    public FilmEditResponse findFilmToEditById(Long id) {
        return filmRepository.findById(id).map(film -> FilmEditResponse.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .director(new SelectOptionResponse(film.getDirector().getId().toString(),
                                film.getDirector().getName()))
                        .publishDate(film.getPublishDate())
                        .actors(film.getFilmActors().stream().map(filmActor ->
                                new SelectOptionResponse(filmActor.getActor().getId().toString(),
                                        filmActor.getActor().getName())).toList())
                        .categories(film.getFilmCategories().stream().map(filmCategory ->
                                new SelectOptionResponse(filmCategory.getCategory().getId().toString(),
                                        filmCategory.getCategory().getName())).toList())
                        .build()

                )
                .orElseThrow(() -> new RuntimeException("Id not found"));
    }

    public boolean updateFilm(Long id, FilmSaveRequest updatedFilm) {
        Film filmDB = findById(id);

        filmDB.setName(updatedFilm.getName());
        filmDB.setPublishDate(AppUtil.mapper.map(updatedFilm.getPublishDate(), LocalDate.class));
        filmDB.setDirector(new Person(Long.parseLong(updatedFilm.getDirector().getId())));
        filmRepository.save(filmDB);

        filmCategoryService.deleteByFilmId(filmDB.getId());
        filmActorService.deleteByFilmId(filmDB.getId());

        var filmCategories = new ArrayList<FilmCategory>();

        for (String idCategory : updatedFilm.getCategories()) {
            Category category = new Category(Long.valueOf(idCategory));
            filmCategories.add(new FilmCategory(filmDB, category));
        }
        filmCategoryRepository.saveAll(filmCategories);

        var filmActors = new ArrayList<FilmActor>();
        List<Person> actors = new ArrayList<>();
        for (String idActor : updatedFilm.getActors()) {
            Person actor = new Person(Long.valueOf(idActor));
            filmActors.add(new FilmActor(actor, filmDB));
        }
        filmActorRepository.saveAll(filmActors);
        return true;
    }

    public boolean deleteFilm(Long id) {
        filmCategoryService.deleteByFilmId(id);
        filmActorService.deleteByFilmId(id);
        filmRepository.deleteById(id);
        return true;
    }
}

