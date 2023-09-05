package com.example.film.service.filmService.request;

import com.example.film.service.request.SelectOptionRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Set;
@Data
@NoArgsConstructor
public class FilmSaveRequest {
    @NotBlank
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String publishDate;

    private SelectOptionRequest director;

    private Set<@NotNull(message = "Not null") String> categories;

    private Set<String> actors;
}
