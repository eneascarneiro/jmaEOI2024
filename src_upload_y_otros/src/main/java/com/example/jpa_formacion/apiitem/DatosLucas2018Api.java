package com.example.jpa_formacion.apiitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosLucas2018Api {
    private Integer keyapi;
    private String longitud ;
    private String latitud ;
    private String id;
    private String path;
    private String reflectance;
    private String band;
    private String survey_date;
    private String oc;
}
