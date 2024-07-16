package com.example.jpa_formacion.service;

import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.PythonScriptDto;
import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.PythonScript;
import com.example.jpa_formacion.repository.EvalScriptRepository;
import com.example.jpa_formacion.repository.PythonScriptRepository;
import com.example.jpa_formacion.service.mapper.EvalScriptMapper;
import com.example.jpa_formacion.service.mapper.PythonScriptMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class PythonScriptService extends AbstractBusinessService<PythonScript,Integer, PythonScriptDto,
        PythonScriptRepository, PythonScriptMapper>   {
    //


    //Acceso a los datos de la bbdd
    public PythonScriptService(PythonScriptRepository repo, PythonScriptMapper serviceMapper) {

        super(repo, serviceMapper);
    }
    public PythonScriptDto guardar(PythonScriptDto dto){
        //Traduzco del dto con datos de entrada a la entidad
        final PythonScript entidad = getMapper().toEntity(dto);
        //Guardo el la base de datos
        PythonScript entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<PythonScriptDto> ldto){
        Iterator<PythonScriptDto> it = ldto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            PythonScript ent = getMapper().toEntity(it.next());
            getRepo().save(ent);
        }
    }

    public Page<PythonScriptDto> buscarTodosPorUsuarioId(PageRequest of, Integer id) {
        return this.getRepo().findPythonScriptByUsuarioPythonScript_Id(of, id).map(this.getMapper()::toDto);
    }
}
