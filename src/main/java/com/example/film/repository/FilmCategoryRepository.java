package com.example.film.repository;


import com.example.film.domain.FilmCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM FilmCategory fc WHERE fc.film.id = :filmId")
    void deleteFilmCategoriesByFilmId( Long filmId);
}
