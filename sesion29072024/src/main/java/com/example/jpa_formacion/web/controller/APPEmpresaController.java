package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.dto.UsuarioDto;
import com.example.jpa_formacion.model.Empresa;
import com.example.jpa_formacion.repository.EmpresaRepository;
import com.example.jpa_formacion.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class APPEmpresaController  extends AbstractController <Empresa> {

    @Autowired
    EmpresaRepository empresaRepository;

    protected APPEmpresaController(MenuService menuService) {
        super(menuService);
    }

    @GetMapping("/empresas/listaempresas")
    public String getAllEmpresas(ModelMap interfazConPantalla) {
        //generamos el contenedor
        List<Empresa> empresas = new ArrayList<>();

        //Leemos dentro del array el contenido del modelo virtual desde el repositorio
        empresaRepository.findAll().forEach(empresas::add);
        interfazConPantalla.addAttribute("lista", empresas);
        return "empresas/all";
    }

}
