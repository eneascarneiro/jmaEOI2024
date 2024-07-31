package com.example.jpa_formacion.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFilesContentDto {
    private String longitude;
    private String latitude;
    private String soc;
    private String val1;
    private String val2;
    private String val3;
    private String date;

}
