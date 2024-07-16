package com.example.jpa_formacion.repository;

import com.example.jpa_formacion.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //Añadir findall
    Page<Role> findAll(Pageable pageable);

    List<Role>  findAllByShowOnCreate(Integer val);
}
