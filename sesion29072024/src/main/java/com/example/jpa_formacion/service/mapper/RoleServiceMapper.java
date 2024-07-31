package com.example.jpa_formacion.service.mapper;


import com.example.jpa_formacion.dto.RoleDTO;
import com.example.jpa_formacion.model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceMapper extends AbstractServiceMapper<Role, RoleDTO> {

    @Override
    public Role toEntity(RoleDTO dto) {
        final Role entidad = new Role();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return entidad;
    }
    @Override
    public RoleDTO toDto(Role entidad) {
        final RoleDTO dto = new RoleDTO();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    @Override
    public List<RoleDTO> toDto(List<Role> e){
        List<RoleDTO> dtos = new ArrayList<>();
        for (Iterator<Role> iter = e.iterator(); iter.hasNext(); ) {
            Role element = iter.next();
            if (element.getId() != null) {
                RoleDTO roleDTO = new RoleDTO();
                // 1 - can call methods of element
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.map(element, roleDTO);
                // 2 - can use iter.remove() to remove the current element from the list
                dtos.add(roleDTO);
            }
            // ...
        }
        return dtos;
    }
}
