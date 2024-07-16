package com.example.jpa_formacion.service;

import com.example.jpa_formacion.model.Employee;
import com.example.jpa_formacion.model.Vacation;

import java.util.List;

public interface VacationService {
    List<Vacation> getAllVacations();

    List<Vacation> getEmployeeVacations(Employee employee);

    void saveVacation(Vacation vacation, Employee employee);

    Vacation getVacationById(long id);

    void deleteVacationById(long id);
}
