package com.example.jpa_formacion.service.mapper;

import com.example.jpa_formacion.dto.DatosLucas2018Dto;
import com.example.jpa_formacion.model.DatosLucas2018;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DatosLucas2018Mapper extends AbstractServiceMapper<DatosLucas2018, DatosLucas2018Dto> {
    //Convertir de entidad a dtoç
    @Override
    public DatosLucas2018Dto toDto(DatosLucas2018 entidad){
        final DatosLucas2018Dto dto = new DatosLucas2018Dto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public DatosLucas2018 toEntity(DatosLucas2018Dto dto){
        final DatosLucas2018 entidad = new DatosLucas2018();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public DatosLucas2018Mapper() {
    }
}
