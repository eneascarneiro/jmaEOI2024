package com.example.jpa_formacion.service;

import com.example.jpa_formacion.apiitem.Listfiles;
import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.model.FiltroListarArchivos;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.repository.SentinelQueryFilesRepository;
import com.example.jpa_formacion.service.mapper.SentinelQueryFilesMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class SentinelQueryFilesService extends AbstractBusinessService<SentinelQueryFiles,Integer, SentinelQueryFilesDto,
        SentinelQueryFilesRepository, SentinelQueryFilesMapper>   {
    //
    private final UsuarioService usuarioService;

    private final FiltroListarArchivosService filtroListarArchivosService;

    //Acceso a los datos de la bbdd
    public SentinelQueryFilesService(SentinelQueryFilesRepository repo, SentinelQueryFilesMapper serviceMapper, UsuarioService usuarioService, FiltroListarArchivosService filtroListarArchivosService) {

        super(repo, serviceMapper);
        this.usuarioService = usuarioService;
        this.filtroListarArchivosService = filtroListarArchivosService;
    }
    public SentinelQueryFilesDto guardar(SentinelQueryFilesDto dto){
        //Traduzco del dto con datos de entrada a la entidad
        final SentinelQueryFiles entidad = getMapper().toEntity(dto);
        //Guardo el la base de datos
        SentinelQueryFiles entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }

    //Método para guardar una lista de grupos
    //La entrada es una lista de DTO ( que viene de la pantalla)
    //La respuesta tipo void
    @Override
    public void  guardar(List<SentinelQueryFilesDto> ldto){
        Iterator<SentinelQueryFilesDto> it = ldto.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            SentinelQueryFiles ent = getMapper().toEntity(it.next());
            getRepo().save(ent);
        }
    }
    public void  deleteDesdeFiltro(Integer filtroid) throws Exception {

        this.getRepo().deleteSentinelQueryFilesByFiltroListarArchivos_Id(filtroid);
    }
    public void  guardarDesdeConsulta(List<Listfiles> dtos,Integer filtroid) throws Exception {
        Iterator<Listfiles> it = dtos.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //
            Listfiles listfiles = it.next();
            SentinelQueryFiles ent = new SentinelQueryFiles();
            ent.setSentinelId(listfiles.getId());
            ent.setName(listfiles.getName());
            ent.setOnline(listfiles.getOnline());
            ent.setPublicationDate(listfiles.getPublicationDate());
            //System.out.println("list files ent id:" + ent.getSentinelId());
            //System.out.println("list files listfile getFootprint:" + listfiles.getFootprint());
            ent.setFootprint(listfiles.getFootprint());
            ent.setGeofootprint(listfiles.getGeofootprint());
            Optional<FiltroListarArchivos> filtroListarArchivos =
                    filtroListarArchivosService.buscar(filtroid);
            if (filtroListarArchivos.isPresent()) {
                ent.setFiltroListarArchivos(filtroListarArchivos.get());
            }
            this.getRepo().save(ent);
        }
    }


    public Page<SentinelQueryFilesDto> buscarTodosPorFiltroId(PageRequest of, long id) {
        return this.getRepo().findSentinelQueryFilesByFiltroListarArchivos_Id(of, id).map(this.getMapper()::toDto);
    }

    public Page<SentinelQueryFilesDto> buscarTodosPorUserId(PageRequest of, long id) {
        Optional<Usuario> usuario = usuarioService.buscar((int) id);

        return this.getRepo().findSentinelQueryFilesByFiltroListarArchivos_UsuarioFiltro_Id(of, usuario.get().getId()).map(this.getMapper()::toDto);
    }
}
