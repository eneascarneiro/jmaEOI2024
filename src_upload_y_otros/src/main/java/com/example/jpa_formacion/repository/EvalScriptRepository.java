package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.model.SentinelQueryFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvalScriptRepository extends JpaRepository<EvalScript,Integer> {

    //Definir metodo aparte
    Page<EvalScript> findEvalScriptByUsuarioScript_Id(Pageable pageable, long id);
}
