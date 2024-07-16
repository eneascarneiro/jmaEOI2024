package com.example.jpa_formacion.repository;


import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.model.UploadedFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFilesRepository extends JpaRepository<UploadedFiles,Integer> {


    Page<UploadedFiles> findUploadedFilesByUsuarioUpload_Id(Pageable pageable, long id);   //Definir metodo aparte
}
