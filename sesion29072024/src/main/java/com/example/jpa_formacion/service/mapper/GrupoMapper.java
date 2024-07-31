package com.example.jpa_formacion.service.mapper;

import com.example.jpa_formacion.dto.GrupoTrabajoDto;
import com.example.jpa_formacion.model.GrupoTrabajo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GrupoMapper extends AbstractServiceMapper<GrupoTrabajo, GrupoTrabajoDto> {
    //Convertir de entidad a dtoç
    @Override
    public GrupoTrabajoDto toDto(GrupoTrabajo entidad){
        final GrupoTrabajoDto dto = new GrupoTrabajoDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public GrupoTrabajo toEntity(GrupoTrabajoDto dto){
        final GrupoTrabajo entidad = new GrupoTrabajo();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public GrupoMapper() {
    }
}
