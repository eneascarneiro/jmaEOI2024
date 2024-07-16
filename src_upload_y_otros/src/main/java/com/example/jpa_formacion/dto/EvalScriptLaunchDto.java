package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.EvalScriptLaunch;
import com.example.jpa_formacion.model.Usuario;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvalScriptLaunchDto {

    private Integer id;
    private String dateIni;
    private String dateFin;
    private String polygon;
    private String collection;
    private Integer resolution;
    private Integer ofsset;
    private Integer maxcloudcoverage;
    private String pathtiff;
    private String pathjson;
    private EvalScript evalScript;

}
