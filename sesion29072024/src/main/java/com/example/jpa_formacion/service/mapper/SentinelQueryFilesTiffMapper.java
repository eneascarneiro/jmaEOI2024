package com.example.jpa_formacion.service.mapper;

import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesTiffDto;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import com.example.jpa_formacion.model.SentinelQueryFilesTiff;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SentinelQueryFilesTiffMapper extends AbstractServiceMapper<SentinelQueryFilesTiff, SentinelQueryFilesTiffDto> {
    //Convertir de entidad a dtoç
    @Override
    public SentinelQueryFilesTiffDto toDto(SentinelQueryFilesTiff entidad){
        final SentinelQueryFilesTiffDto dto = new SentinelQueryFilesTiffDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public SentinelQueryFilesTiff toEntity(SentinelQueryFilesTiffDto dto){
        final SentinelQueryFilesTiff entidad = new SentinelQueryFilesTiff();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public SentinelQueryFilesTiffMapper() {
    }
}
