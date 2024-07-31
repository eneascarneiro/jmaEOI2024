package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "filtrolistararchivos")
public class FiltroListarArchivos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String reference;
    @Column(nullable = false)
    private String dateIni;
    @Column(nullable = false)
    private String dateFin;
    @Column(nullable = false)
    private String polygon;
    @Column(nullable = false)
    private long cloudCover;
    @Column(nullable = false)
    private Integer nunberOfResults;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioFiltro;



    @OneToMany(mappedBy = "filtroListarArchivos" )
    private Set<SentinelQueryFiles> sentinelQueryFiles;

}
