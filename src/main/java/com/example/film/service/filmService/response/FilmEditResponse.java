package com.example.film.service.filmService.response;

import com.example.film.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmEditResponse {
    private Long id;
    private String name;
    private LocalDate publishDate;
    private SelectOptionResponse director;
    private List<SelectOptionResponse> actors;
    private List<SelectOptionResponse> categories;

}