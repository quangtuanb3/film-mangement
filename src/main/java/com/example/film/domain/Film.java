package com.example.film.domain;

import com.example.film.validateInterface.ValidBoundaryDates;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "films")
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z ]{6,20}")
    private String name;

    //    @Past(message = "Publish date must be in the past")
    @ValidBoundaryDates(startDate = "1990-01-01", endDate = "2023-09-05", message = "in range")
    private LocalDate publishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id")
    private Person director;

    @OneToMany(mappedBy = "film")
    private List<FilmActor> filmActors;

    @OneToMany(mappedBy = "film")
    private List<FilmCategory> filmCategories;
}
