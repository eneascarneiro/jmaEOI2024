package com.example.jpa_formacion.service.mapper;

import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SentinelQueryFilesMapper extends AbstractServiceMapper<SentinelQueryFiles, SentinelQueryFilesDto> {
    //Convertir de entidad a dtoç
    @Override
    public SentinelQueryFilesDto toDto(SentinelQueryFiles entidad){
        final SentinelQueryFilesDto dto = new SentinelQueryFilesDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public SentinelQueryFiles toEntity(SentinelQueryFilesDto dto){
        final SentinelQueryFiles entidad = new SentinelQueryFiles();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public SentinelQueryFilesMapper() {
    }
}
