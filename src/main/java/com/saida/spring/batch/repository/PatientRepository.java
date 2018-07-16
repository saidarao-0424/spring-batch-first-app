package com.saida.spring.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saida.spring.batch.domain.Person;

@Repository
public interface PatientRepository extends JpaRepository<Person, Long> {
}
