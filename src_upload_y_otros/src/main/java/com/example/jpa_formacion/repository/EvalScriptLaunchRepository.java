package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.EvalScriptLaunch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvalScriptLaunchRepository extends JpaRepository<EvalScriptLaunch,Integer> {

    //Definir metodo aparte
}
