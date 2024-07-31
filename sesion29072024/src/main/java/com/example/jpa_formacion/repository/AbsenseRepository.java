package com.example.jpa_formacion.repository;

import com.example.jpa_formacion.model.Absense;

import com.example.jpa_formacion.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenseRepository extends JpaRepository<Absense, Long> {

  List<Absense> findAllByEmployee(Employee employee);

}
