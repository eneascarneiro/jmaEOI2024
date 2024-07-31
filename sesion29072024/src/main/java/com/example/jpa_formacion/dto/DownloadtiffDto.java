package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.EvalScript;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadtiffDto {

    private List<EvalScript> evalScripts;

    private String file;

}
