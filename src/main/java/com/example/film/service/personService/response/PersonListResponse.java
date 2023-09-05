package com.example.film.service.personService.response;

import com.example.film.domain.*;
import com.example.film.domain.enums.EGender;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonListResponse {
    private String id;
    private String name;
    private LocalDate dob;
    private EGender gender;
    private Set<Role> roles;
}
