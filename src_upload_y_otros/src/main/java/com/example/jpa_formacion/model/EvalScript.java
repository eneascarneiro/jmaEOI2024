package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "evalscript")
public class EvalScript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String scriptTitle;
    @Column(nullable = false)
    private String scriptDescription;
    @Column(nullable = false)
    private String scriptText;
    @Column(nullable = false)
    private boolean shared = true;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioScript;


    @OneToMany(mappedBy = "evalScript" )
    private Set<EvalScriptLaunch> evalScriptsLaunches;

}
