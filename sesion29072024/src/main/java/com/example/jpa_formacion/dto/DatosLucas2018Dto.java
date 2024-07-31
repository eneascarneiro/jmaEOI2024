package com.example.jpa_formacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosLucas2018Dto {
    private Long id;
    private Integer searchid;
    private Integer keyapi;
    private String longitud ;
    private String latitud ;
    private String pointid;
    private String path;
    private String reflectance;
    private String band;
    private String survey_date;
    private String oc;
}
