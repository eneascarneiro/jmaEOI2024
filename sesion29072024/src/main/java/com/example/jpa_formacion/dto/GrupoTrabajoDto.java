package com.example.jpa_formacion.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrupoTrabajoDto {
    private long id;

    private String descripcion;

    private String description_es;

    private String description_en;

    private String description_fr;

    private String description_01;

    private String description_02;

    private String description_03;

    private String description_04;

    private String email;

    private String url;

}
