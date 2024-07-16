package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.FiltroListarArchivos;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiltroListarArchivosRepository extends JpaRepository<FiltroListarArchivos,Integer> {

    //Definir metodo aparte
    //Definir metodo aparte
    Page<FiltroListarArchivos> findFiltroListarArchivosByUsuarioFiltro_Id(Pageable pageable, long id);
}
