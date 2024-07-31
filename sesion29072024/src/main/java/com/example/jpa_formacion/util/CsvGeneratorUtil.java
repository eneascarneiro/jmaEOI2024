package com.example.jpa_formacion.util;

import com.example.jpa_formacion.apiitem.DatosLucas2018Api;
import com.example.jpa_formacion.apiitem.UploadedFilesReflectance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvGeneratorUtil {
    private static final String CSV_HEADER = "Longitude,Latitude,Id,Band,Reflectance,Coarse_mas,Coarse_vol\n";

    public String generateCsv(List<DatosLucas2018Api> lista) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (DatosLucas2018Api datosLucas2018Api : lista) {
            csvContent.append(datosLucas2018Api.getLongitud()).append(",")
                    .append(datosLucas2018Api.getLatitud()).append(",")
                    .append(datosLucas2018Api.getId()).append(",")
                    .append(datosLucas2018Api.getBand()).append(",")
                    .append(datosLucas2018Api.getReflectance()).append(",")
                    .append(datosLucas2018Api.getOc()).append("\n");
        }

        return csvContent.toString();
    }
    public String generateCsvdatosusr(List<UploadedFilesReflectance> lista) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (UploadedFilesReflectance uploadedFilesReflectance : lista) {
            csvContent.append(uploadedFilesReflectance.getLongitude()).append(",")
                    .append(uploadedFilesReflectance.getLatitude()).append(",")
                    .append(uploadedFilesReflectance.getId()).append(",")
                    .append(uploadedFilesReflectance.getBand()).append(",")
                    .append(uploadedFilesReflectance.getReflectance()).append(",")
                    .append(uploadedFilesReflectance.getSoc()).append("\n");
        }

        return csvContent.toString();
    }
}