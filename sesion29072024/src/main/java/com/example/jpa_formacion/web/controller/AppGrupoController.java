package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.dto.GrupoTrabajoDto;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.service.GrupoService;
import com.example.jpa_formacion.service.MenuService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AppGrupoController extends AbstractController <GrupoTrabajoDto> {

    private final GrupoService service;


    public AppGrupoController(MenuService menuService, GrupoService service) {
        super(menuService);
        this.service = service;
    }

    @GetMapping("/grupos")
    public String vistaGrupos(@RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                    ModelMap interfazConPantalla){


        //tenemos que leer la lista de usuarios
        //Que elemento me la ofrece?
        //listaUsrTodos
        //List<UsuarioDto>  lusrdto = this.service.listaUsrTodos();
        //interfazConPantalla.addAttribute("listausuarios", lusrdto);
        //Obetenemos el objeto Page del servicio
        Integer pagina = 0;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 10;
        if (size.isPresent()) {
            maxelementos = size.get();
        }

        Page<GrupoTrabajoDto> grupoDtoPage =
                this.service.buscarTodos(PageRequest.of(pagina,maxelementos));
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(grupoDtoPage));
        interfazConPantalla.addAttribute("listaGrupos", grupoDtoPage);
        return "grupos/listagrupospagina";
    }

    @GetMapping("/grupos/{idusr}")
    @PostAuthorize("hasRole('ADMIN')")
    public String vistaDatosGrupo(@PathVariable("idusr") Integer id, ModelMap interfazConPantalla){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<GrupoTrabajoDto> grupoDto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (grupoDto.isPresent()){
            //Como encontré datos, obtengo el objerto de tipo "UsuarioDto"
            //addAttribute y thymeleaf no  entienden Optional
            GrupoTrabajoDto attr = grupoDto.get();
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("datos",attr);

            return "grupos/edit";
        } else{
            //Mostrar página usuario no existe
            return "grupos/detallesgruponoencontrado";
        }
    }


    @PostMapping("/grupos/{idusr}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarDatosGrupo(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<GrupoTrabajoDto> grupoDto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (grupoDto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/grupos";
        } else{
            //Mostrar página usuario no existe
            return "grupos/detallesgruponoencontrado";
        }
    }
    @PostMapping("/grupos/{idusr}/habilitar")
    @PreAuthorize("hasRole('ADMIN')")
    public String habilitarDatosGrupo(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<GrupoTrabajo> grupo = this.service.encuentraPorIdEntity(id);
        //¿Debería comprobar si hay datos?
        if (grupo.isPresent()){
            GrupoTrabajo attr = grupo.get();
            if (attr.isActive())
                attr.setActive(false);
            else
                attr.setActive(true);
            this.service.getRepo().save(attr);
            //Mostrar listado de usuarios
            return "redirect:/grupos";
        } else{
            //Mostrar página usuario no existe
            return "grupos/detallesgruponoencontrado";
        }
    }

    //Postmaping para guardar
    @PostMapping("/grupos/{idusr}")
    public String guardarEdicionDatosGrupo(@PathVariable("idusr") Integer id, GrupoTrabajoDto grupoTrabajoDtoEntrada) throws Exception {
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<GrupoTrabajoDto> grupoDtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (grupoDtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            grupoTrabajoDtoEntrada.setId(id);
            this.service.guardar(grupoTrabajoDtoEntrada);

            return String.format("redirect:/grupos/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "grupos/detallesgruponoencontrado";
        }
    }

    //Para crear un grupo hay dos bloques
    //El que genera la pantalla para pedir los datos de tipo GetMapping
    //Cuando pasamos informacion a la pantalla hay que usar ModelMap
    @GetMapping("/grupos/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final GrupoTrabajoDto grupoTrabajoDto = new GrupoTrabajoDto();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datos", grupoTrabajoDto);
        return "grupos/registro";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/grupos/registro")
    public String guardarUsuario( @ModelAttribute(name ="datosUsuario") GrupoTrabajoDto gruposDto) throws Exception {
        //Comprobamos el patron
        GrupoTrabajoDto grupoTrabajoDto1 = this.service.guardar(gruposDto);
        return String.format("redirect:/grupos/%s", grupoTrabajoDto1.getId());
    }
}
