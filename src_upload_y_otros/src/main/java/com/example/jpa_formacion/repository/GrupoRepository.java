package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.GrupoTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<GrupoTrabajo,Integer> {

    //Definir metodo aparte
}
