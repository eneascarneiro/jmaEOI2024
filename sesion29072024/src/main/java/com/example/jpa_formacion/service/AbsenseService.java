package com.example.jpa_formacion.service;

import com.example.jpa_formacion.model.Absense;
import com.example.jpa_formacion.model.Employee;

import java.util.List;

public interface AbsenseService {
    List<Absense> getAllAbsenses();

    List<Absense> getEmployeeAbsenses(Employee employee);

    void saveAbsense(Absense absense, Employee employee);

    Absense getAbsenseById(long id);

    void deleteAbsenseById(long id);
}
