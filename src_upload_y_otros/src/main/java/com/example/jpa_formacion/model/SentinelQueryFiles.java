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
@Table(name = "sentinelqueryfiles")
public class SentinelQueryFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String sentinelId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String online;
    @Column(nullable = false)
    private String publicationDate;
    @Column(nullable = true,length = 5000)
    private String footprint;
    @Column(nullable = true,length = 5000)
    private String geofootprint;
    @Column(nullable = false)
    private Integer nunberOfTiff = 0;

    @OneToMany(mappedBy = "sentinelQueryFilesfortiff" )
    private Set<SentinelQueryFilesTiff> sentinelQueryFilesTiffs ;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "filter_id")
    private FiltroListarArchivos filtroListarArchivos;

}
