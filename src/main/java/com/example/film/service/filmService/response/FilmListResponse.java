package com.example.film.service.filmService.response;

import com.example.film.domain.Category;
import com.example.film.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmListResponse {
    private Long id;
    private String name;
    private LocalDate publishDate;
    private String director;
    private String actors;
    private String categories;

}
