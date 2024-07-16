package com.example.jpa_formacion.repository;



import com.example.jpa_formacion.model.SentinelQueryFiles;
import com.example.jpa_formacion.model.SentinelQueryFilesTiff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentinelQueryFilesTiffRepository extends JpaRepository<SentinelQueryFilesTiff,Integer> {

    //Definir metodo aparte
    void deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_Id(Integer id);

    void deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_FiltroListarArchivos_Id(Integer id);


    Page<SentinelQueryFilesTiff> findSentinelQueryFilesBySentinelQueryFilesfortiff_IdAndPathLike(Pageable pageable,long id,String patron);

    List<SentinelQueryFilesTiff> findSentinelQueryFilesTiffBySentinelQueryFilesfortiff_IdAndPathLike (long id,String patron);

    Page<SentinelQueryFilesTiff> findSentinelQueryFilesBySentinelQueryFilesfortiff_FiltroListarArchivos_UsuarioFiltro_Id(Pageable pageable, long usuario);
}
