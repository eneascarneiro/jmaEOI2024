package com.example.jpa_formacion.service;

import com.example.jpa_formacion.dto.GrupoTrabajoDto;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.repository.GrupoRepository;
import com.example.jpa_formacion.service.mapper.GrupoMapper;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class GrupoService extends AbstractBusinessService<GrupoTrabajo,Integer, GrupoTrabajoDto,
        GrupoRepository, GrupoMapper>   {
    //


    //Acceso a los datos de la bbdd
    public GrupoService(GrupoRepository repo, GrupoMapper serviceMapper) {

        super(repo, serviceMapper);
    }
    public GrupoTrabajoDto guardar(GrupoTrabajoDto grupoTrabajoDto){
        //Traduzco del dto con datos de entrada a la entidad
        final GrupoTrabajo entidad = getMapper().toEntity(grupoTrabajoDto);
        //Guardo el la base de datos
        GrupoTrabajo entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<GrupoTrabajoDto> lgrupoTrabajoDto){
        Iterator<GrupoTrabajoDto> it = lgrupoTrabajoDto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            GrupoTrabajo grupo = getMapper().toEntity(it.next());
            getRepo().save(grupo);
        }
    }


}
