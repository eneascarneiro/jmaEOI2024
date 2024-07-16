package com.example.jpa_formacion.service.mapper;

import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.EvalScriptLaunchDto;
import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.EvalScriptLaunch;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EvalScriptLaunchMapper extends AbstractServiceMapper<EvalScriptLaunch, EvalScriptLaunchDto> {
    //Convertir de entidad a dtoç
    @Override
    public EvalScriptLaunchDto toDto(EvalScriptLaunch entidad){
        final EvalScriptLaunchDto dto = new EvalScriptLaunchDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public EvalScriptLaunch toEntity(EvalScriptLaunchDto dto){
        final EvalScriptLaunch entidad = new EvalScriptLaunch();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public EvalScriptLaunchMapper() {
    }
}
