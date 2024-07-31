package com.example.jpa_formacion.service;

import com.example.jpa_formacion.apiitem.Listfiles;
import com.example.jpa_formacion.apiitem.Listfilestiff;
import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesTiffDto;
import com.example.jpa_formacion.model.FiltroListarArchivos;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import com.example.jpa_formacion.model.SentinelQueryFilesTiff;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.repository.SentinelQueryFilesRepository;
import com.example.jpa_formacion.repository.SentinelQueryFilesTiffRepository;
import com.example.jpa_formacion.service.mapper.SentinelQueryFilesMapper;
import com.example.jpa_formacion.service.mapper.SentinelQueryFilesTiffMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class SentinelQueryFilesTiffService extends AbstractBusinessService<SentinelQueryFilesTiff,Integer, SentinelQueryFilesTiffDto,
        SentinelQueryFilesTiffRepository, SentinelQueryFilesTiffMapper>   {
    //
    private final UsuarioService usuarioService;

    private final SentinelQueryFilesService sentinelQueryFilesService;

    //Acceso a los datos de la bbdd
    public SentinelQueryFilesTiffService(SentinelQueryFilesTiffRepository repo, SentinelQueryFilesTiffMapper serviceMapper, UsuarioService usuarioService, SentinelQueryFilesService sentinelQueryFilesService) {

        super(repo, serviceMapper);
        this.usuarioService = usuarioService;
        this.sentinelQueryFilesService = sentinelQueryFilesService;
    }
    public SentinelQueryFilesTiffDto guardar(SentinelQueryFilesTiffDto dto){
        //Traduzco del dto con datos de entrada a la entidad
        final SentinelQueryFilesTiff entidad = getMapper().toEntity(dto);
        //Guardo el la base de datos
        SentinelQueryFilesTiff entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<SentinelQueryFilesTiffDto> ldto){
        Iterator<SentinelQueryFilesTiffDto> it = ldto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            SentinelQueryFilesTiff ent = getMapper().toEntity(it.next());
            getRepo().save(ent);
        }
    }
    public void  deleteDesdeFiltro(Integer filtroid) throws Exception {
        this.getRepo().deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_Id(filtroid);
    }
    public void  guardarDesdeConsulta(List<Listfilestiff> dtos, Integer id) throws Exception {
        Iterator<Listfilestiff> it = dtos.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //
            Listfilestiff listfiles = it.next();
            SentinelQueryFilesTiff ent = new SentinelQueryFilesTiff();
            ent.setBand(listfiles.getBand());
            ent.setPath(listfiles.getPath());
            Optional<SentinelQueryFiles> sentinelQueryFiles =
                    sentinelQueryFilesService.buscar(id);
            if (sentinelQueryFiles.isPresent()) {
                ent.setSentinelQueryFilesfortiff(sentinelQueryFiles.get());
            }
            this.getRepo().save(ent);
        }
    }

    public Page<SentinelQueryFilesTiffDto> buscarTodosPorFiltroId(PageRequest of, long id) {
        return this.getRepo().findSentinelQueryFilesBySentinelQueryFilesfortiff_IdAndPathLike(of, id,"%.tiff").map(this.getMapper()::toDto);
    }

    public Page<SentinelQueryFilesTiffDto> buscarTodosPorUserId(PageRequest of, long id) {
        Optional<Usuario> usuario = usuarioService.buscar((int) id);

        return this.getRepo().findSentinelQueryFilesBySentinelQueryFilesfortiff_FiltroListarArchivos_UsuarioFiltro_Id(of, usuario.get().getId()).map(this.getMapper()::toDto);
    }

}
