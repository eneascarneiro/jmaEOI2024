package com.example.jpa_formacion.repository;



import com.example.jpa_formacion.model.PythonScript;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PythonScriptRepository extends JpaRepository<PythonScript,Integer> {

    //Definir metodo aparte
    Page<PythonScript>  findPythonScriptByUsuarioPythonScript_Id(Pageable pageable, Integer id);
}
