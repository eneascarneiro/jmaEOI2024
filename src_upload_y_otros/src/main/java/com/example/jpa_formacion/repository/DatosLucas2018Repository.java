package com.example.jpa_formacion.repository;

import com.example.jpa_formacion.model.DatosLucas2018;
import com.example.jpa_formacion.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatosLucas2018Repository extends JpaRepository<DatosLucas2018, Long> {
    List<DatosLucas2018> findBySearchidAndAndPathLike(Integer id , String str);

    DatosLucas2018 findBySearchidAndPointidAndBandAndPathLike(Integer id ,String pointid,  String band, String str);

}
