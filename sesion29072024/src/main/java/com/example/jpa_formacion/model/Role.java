package com.example.jpa_formacion.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roleName;

    @Column(nullable = false)
    private Integer showOnCreate;

    @Column(nullable = false)
    private String roleName_en;
    @Column(nullable = false)
    private String roleName_es;
    @Column(nullable = false)
    private String roleName_fr;
    @Column(nullable = false)
    private String roleName_01;
    @Column(nullable = false)
    private String roleName_02;
    @Column(nullable = false)
    private String roleName_03;
    @Column(nullable = false)
    private String roleName_04;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
        private Set<Usuario>  usuarios;

}
