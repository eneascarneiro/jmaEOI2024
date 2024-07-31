package com.example.jpa_formacion.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class RoleDTO implements Serializable {
    private Long id;
    private String roleName;

    private String roleName_en;
    private String roleName_es;
    private String roleName_fr;
    private String roleName_01;
    private String roleName_02;
    private String roleName_03;
    private String roleName_04;
}
