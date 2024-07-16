package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.config.service.UserService;
import com.example.jpa_formacion.dto.*;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.GrupoService;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.RoleService;
import com.example.jpa_formacion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.jpa_formacion.util.ValidarFormatoPassword.ValidarFormato;

@Controller
public class AppUsuariosSeguridadController extends AbstractController <UsuarioDto> {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    private final  UsuarioService service;
    private final RoleService roleService;

    private final GrupoService grupoService;

    public AppUsuariosSeguridadController(MenuService menuService, UsuarioService service, RoleService roleService, GrupoService grupoService) {
        super(menuService);
        this.service = service;
        this.roleService = roleService;
        this.grupoService = grupoService;
    }


    // Read Form data to save into DB

    //Para crear un usuario hay dos bloques
    //El que genera la pantalla para pedir los datos de tipo GetMapping
    //Cuando pasamos informacion a la pantalla hay que usar ModelMap
    @GetMapping("/usuarios/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final UsuarioDtoPsw usuarioDtoPsw = new UsuarioDtoPsw();
        //Obtengo la lista de roles
        final List<RoleDTO> roleDTOList = roleService.buscarTodosAlta();
        //Obtengo la lista de grupos
        final List<GrupoTrabajoDto> grupoTrabajoDtos = grupoService.buscarTodos();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datosUsuario",usuarioDtoPsw);
        interfazConPantalla.addAttribute("listaRoles",roleDTOList);
        interfazConPantalla.addAttribute("listagrupos",grupoTrabajoDtos);
        System.out.println("Preparando pantalla registro");
        return "usuarios/registro";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/usuarios/registro")
    public String guardarUsuario( @ModelAttribute(name ="datosUsuario") UsuarioDtoPsw usuarioDtoPsw) throws Exception {
        //Comprobamos el patron
        System.out.println("Guardando usuario antes ");
        System.out.println("Usuario :" + usuarioDtoPsw.getNombreUsuario() + ", password : " + usuarioDtoPsw.getPassword() );
        if (ValidarFormato(usuarioDtoPsw.getPassword())){
            Usuario usuario = service.getMapper().toEntityPsw(usuarioDtoPsw);
            System.out.println("Guardando usuario");
            System.out.println("Usuario :" + usuario.getNombreUsuario() + ", password : " + usuario.getPassword() );
            //Codifico la password
            String encodedPasswod = userService.getEncodedPassword(usuario);
            usuarioDtoPsw.setPassword(encodedPasswod);
            //El usuario se guarda como no autorizado
            //Guardo la password
            try {
                UsuarioDto usuario1 = this.service.guardar(usuarioDtoPsw);
                //return "usuarios/detallesusuario";
                return String.format("redirect:/usuarios/%s", usuario1.getId());
            }
            catch (Exception ex){
                System.out.println("Excepcion:" + ex.getMessage());
                if ( ex.getMessage().contains("idx_usuario_email"))
                {
                    return "usuarios/registroemailduplicado";

                }
                else
                    throw ex ;
            }

        }
        else
        {
            return "usuarios/registro";
        }

    }
}
