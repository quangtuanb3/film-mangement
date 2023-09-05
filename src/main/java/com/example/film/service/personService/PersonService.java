package com.example.film.service.personService;

import com.example.film.domain.*;
import com.example.film.repository.*;
import com.example.film.service.personService.response.PersonListResponse;
import com.example.film.service.response.SelectOptionResponse;
import com.example.film.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<PersonListResponse> findAll() {
        return personRepository.findAll()
                .stream()
                .map(person -> {
                    var result = AppUtil.mapper.map(person, PersonListResponse.class);

                    // Map roles without personRoles
                    Set<Role> rolesWithoutPersonRoles = person.getPersonRoles()
                            .stream()
                            .map(PersonRole::getRole)
                            .peek(role -> role.setPersonRoles(null)) // Remove personRoles from each role
                            .collect(toSet());

                    result.setRoles(rolesWithoutPersonRoles);

                    return result;
                })
                .toList();
    }


    public List<SelectOptionResponse> getPersonsByRole(String role){
        return personRepository.getPersonsByRole(role)
                .stream()
                .map(element -> new SelectOptionResponse(element.getId().toString(), element.getName()))
                .collect(Collectors.toList());
    }

    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
    }
}


