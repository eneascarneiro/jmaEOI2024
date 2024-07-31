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
public class UsuarioDto {
    private long id;

    private String email;

    private String nombreUsuario;

    private String nombreEmail;

    private GrupoTrabajo grupoTrabajo;

    private Set<Role> roles = new HashSet<>();
}