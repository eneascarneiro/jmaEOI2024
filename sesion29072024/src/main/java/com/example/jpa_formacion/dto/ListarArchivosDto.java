package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListarArchivosDto {

    private String userName;

    private Long groupid;

    private Long  userid;

    private Integer  filterid;

    private String reference;

    private String dateIni;

    private String dateFin;

    private String polygon;

    private long cloudCover;

    private boolean removePrevData;
}
