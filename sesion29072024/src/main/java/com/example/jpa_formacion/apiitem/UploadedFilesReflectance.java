package com.example.jpa_formacion.apiitem;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFilesReflectance {
    private Integer key;
    private String longitude;
    private String latitude;
    private String reflectance;
    private String userid;
    private String band;
    private String id;
    private String path;
    private String soc;
}
