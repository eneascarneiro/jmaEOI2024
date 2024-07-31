package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.Employee;
import com.example.jpa_formacion.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

  List<Vacation> findAllByEmployee(Employee employee);

}
