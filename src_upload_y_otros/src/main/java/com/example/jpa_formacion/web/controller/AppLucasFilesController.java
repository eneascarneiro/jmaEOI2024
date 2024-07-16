package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.ConfiguationProperties;
import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.SentinelQueryFilesTiffDto;
import com.example.jpa_formacion.model.SentinelQueryFilesTiff;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.SentinelQueryFilesTiffService;
import com.example.jpa_formacion.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AppLucasFilesController extends AbstractController <SentinelQueryFilesTiffDto> {


    private final UsuarioService usuarioService;

    private ConfiguationProperties configuationProperties;

    public AppLucasFilesController(MenuService menuService, SentinelQueryFilesTiffService service, UsuarioService usuarioService, ConfiguationProperties configuationProperties) {
        super(menuService);
        this.configuationProperties = configuationProperties;
        this.usuarioService = usuarioService;
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @GetMapping("/lucasfiles/show/{pathfile}")
    public String showFile(@PathVariable("pathfile") String path,
                                 Model interfazConPantalla) throws Exception {

       if (path.isEmpty() ){
           return "lucas/detallesimagennoencontrado";
        } else {
            //Mostrar página usuario no existe
            return String.format("redirect:/visor/image/lucas/%s", path);
        }
    }
}