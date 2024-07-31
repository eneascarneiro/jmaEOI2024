package com.example.jpa_formacion.service;

import com.example.jpa_formacion.dto.DatosLucas2018Dto;
import com.example.jpa_formacion.model.DatosLucas2018;
import com.example.jpa_formacion.repository.DatosLucas2018Repository;
import com.example.jpa_formacion.service.mapper.DatosLucas2018Mapper;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class DatosLucas2018Service extends AbstractBusinessService<DatosLucas2018,Long, DatosLucas2018Dto,
        DatosLucas2018Repository, DatosLucas2018Mapper>   {
    //


    //Acceso a los datos de la bbdd
    public DatosLucas2018Service(DatosLucas2018Repository repo, DatosLucas2018Mapper serviceMapper) {

        super(repo, serviceMapper);
    }
    public DatosLucas2018Dto guardar(DatosLucas2018Dto dto){
        //Traduzco del dto con datos de entrada a la entidad
        final DatosLucas2018 entidad = getMapper().toEntity(dto);
        //Guardo el la base de datos
        DatosLucas2018 entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<DatosLucas2018Dto> ldto){
        Iterator<DatosLucas2018Dto> it = ldto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            DatosLucas2018 ent = getMapper().toEntity(it.next());
            getRepo().save(ent);
        }
    }
    public List<DatosLucas2018Dto> getlucasfloat (Integer id , String str){
        return  getMapper().toDto(getRepo().findBySearchidAndAndPathLike(id, str));

    }

    public DatosLucas2018 getlucasreglike(Integer id ,String pointid,  String band , String str){
        return getRepo().findBySearchidAndPointidAndBandAndPathLike(id,pointid, band, str);
    }

}
