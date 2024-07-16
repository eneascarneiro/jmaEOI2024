package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.EvalScript;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosLucasSearchDto {

    private Integer id;
    private String dateIni;
    private String dateFin;
    private String polygon;
    private String dataset;

}
