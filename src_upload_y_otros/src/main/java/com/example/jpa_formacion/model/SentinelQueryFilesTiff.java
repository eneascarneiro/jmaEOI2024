package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sentinelqueryfilestiff")
public class SentinelQueryFilesTiff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String band;
    @Column(nullable = false,length = 500)
    private String path;


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "query_id")
    private SentinelQueryFiles sentinelQueryFilesfortiff;

}
