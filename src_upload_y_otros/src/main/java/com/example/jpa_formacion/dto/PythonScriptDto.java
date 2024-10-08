package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PythonScriptDto {

    private Integer id;

    private String scriptTitle;

    private String scriptDescription;

    private String scriptText;

    private Usuario usuarioPythonScript;

}
