package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "datoslucas2018")
public class DatosLucas2018 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer searchid;
    @Column
    private Integer keyapi;
    @Column
    private String longitud ;
    @Column
    private String latitud ;
    @Column
    private String pointid;
    @Column
    private String path;
    @Column
    private String reflectance;
    @Column
    private String band;
    @Column
    private String survey_date;
    @Column
    private String oc;
}
