package com.example.jpa_formacion.dto;

import com.example.jpa_formacion.model.SentinelQueryFiles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SentinelQueryFilesTiffDto {
    private Integer id;

    private String band;

    private String path;

    private SentinelQueryFiles sentinelQueryFilesfortiff;

}
