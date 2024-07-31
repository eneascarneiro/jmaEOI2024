package com.example.jpa_formacion.service;

import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.GrupoTrabajoDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.repository.EvalScriptRepository;
import com.example.jpa_formacion.repository.GrupoRepository;
import com.example.jpa_formacion.service.mapper.EvalScriptMapper;
import com.example.jpa_formacion.service.mapper.GrupoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class EvalScriptService extends AbstractBusinessService<EvalScript,Integer, EvalScriptDto,
        EvalScriptRepository, EvalScriptMapper>   {
    //


    //Acceso a los datos de la bbdd
    public EvalScriptService(EvalScriptRepository repo, EvalScriptMapper serviceMapper) {

        super(repo, serviceMapper);
    }
    public EvalScriptDto guardar(EvalScriptDto dto){
        //Traduzco del dto con datos de entrada a la entidad
        final EvalScript entidad = getMapper().toEntity(dto);
        //Guardo el la base de datos
        EvalScript entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<EvalScriptDto> ldto){
        Iterator<EvalScriptDto> it = ldto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            EvalScript ent = getMapper().toEntity(it.next());
            getRepo().save(ent);
        }
    }

    public Page<EvalScriptDto> buscarTodosPorUsuarioId(PageRequest of, long id) {
        return this.getRepo().findEvalScriptByUsuarioScript_Id(of, id).map(this.getMapper()::toDto);
    }
}
