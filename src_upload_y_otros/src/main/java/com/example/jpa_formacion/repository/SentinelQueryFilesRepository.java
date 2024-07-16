package com.example.jpa_formacion.repository;



import com.example.jpa_formacion.model.SentinelQueryFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentinelQueryFilesRepository extends JpaRepository<SentinelQueryFiles,Integer> {

    //Definir metodo aparte
    Page<SentinelQueryFiles> findSentinelQueryFilesByFiltroListarArchivos_Id(Pageable pageable,long id);


    Page<SentinelQueryFiles> findSentinelQueryFilesByFiltroListarArchivos_UsuarioFiltro_Id(Pageable pageable, long usuario);

    void deleteSentinelQueryFilesByFiltroListarArchivos_Id(Integer id);
}
