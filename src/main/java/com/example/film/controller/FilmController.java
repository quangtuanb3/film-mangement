package com.example.film.controller;

import com.example.film.domain.Film;
import com.example.film.domain.Person;
import com.example.film.repository.FilmRepository;
import com.example.film.repository.PersonRepository;
import com.example.film.service.categoryService.CategoryService;
import com.example.film.service.filmService.FilmService;
import com.example.film.service.filmService.request.FilmSaveRequest;
import com.example.film.service.filmService.response.FilmListResponse;
import com.example.film.service.personService.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class FilmController {
    private final FilmService filmService;
    private final CategoryService categoryService;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final FilmRepository filmRepository;

    @GetMapping
    public ModelAndView showList() {
        ModelAndView view = new ModelAndView("film/index");
        view.addObject("categories", categoryService.findAll());
        view.addObject("directors", personService.getPersonsByRole("Director"));
        view.addObject("actors", personService.getPersonsByRole("Actor"));
        return view;
    }


}
