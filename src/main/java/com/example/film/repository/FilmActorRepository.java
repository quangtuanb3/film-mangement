package com.example.film.repository;

import com.example.film.domain.FilmActor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FilmActorRepository extends JpaRepository<FilmActor, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM FilmActor fa WHERE fa.film.id = :filmId")
    void deleteFilmActorsByFilmId(Long filmId);

}
