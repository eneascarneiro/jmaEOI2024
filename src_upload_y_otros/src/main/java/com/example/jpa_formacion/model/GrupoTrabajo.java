package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grupos")
public class GrupoTrabajo implements Serializable {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column (name ="descripcion",length = 250)
    private String descripcion;
    @Column(nullable = false,length = 250)
    private String description_es;
    @Column(nullable = false,length = 250)
    private String description_en;
    @Column(nullable = false,length = 250)
    private String description_fr;
    @Column(length = 250)
    private String description_01="defaulf";
    @Column(length = 250)
    private String description_02="defaulf";
    @Column(length = 250)
    private String description_03="defaulf";
    @Column(length = 250)
    private String description_04="defaulf";
    @Column (name ="email",length = 30)
    private String email;
    @Column (name ="url",length = 250)
    private String url;
    @OneToMany(mappedBy = "grupoTrabajo", cascade = CascadeType.ALL)
    private Set<Usuario> usuarios;
    @Basic()
    private boolean active;
}

