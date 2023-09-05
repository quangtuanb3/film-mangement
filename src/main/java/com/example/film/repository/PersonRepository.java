package com.example.film.repository;

import com.example.film.domain.FilmActor;
import com.example.film.domain.Person;
import com.example.film.domain.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT p FROM Person p " +
            "JOIN PersonRole pr ON p.id = pr.person.id WHERE  pr.role.name = :name")
    List<Person> getPersonsByRole(String name);
}
