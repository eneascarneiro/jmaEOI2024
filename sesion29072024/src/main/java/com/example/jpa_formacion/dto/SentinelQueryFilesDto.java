package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.FiltroListarArchivos;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SentinelQueryFilesDto {
    private Integer id;
    private String sentinelId;
    private String name;
    private String online;
    private String publicationDate;
    private String footprint;
    private String geofootprint;
    private Integer nunberOfTiff;
    private FiltroListarArchivos filtroListarArchivos;
}
