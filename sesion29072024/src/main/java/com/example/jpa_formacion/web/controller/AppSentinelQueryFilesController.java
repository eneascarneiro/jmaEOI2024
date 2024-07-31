package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesDto;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.EvalScriptService;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.SentinelQueryFilesService;
import com.example.jpa_formacion.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AppSentinelQueryFilesController extends AbstractController <SentinelQueryFilesDto> {

    private final SentinelQueryFilesService service;

    private final UsuarioService usuarioService;


    public AppSentinelQueryFilesController(MenuService menuService, SentinelQueryFilesService service, UsuarioService usuarioService) {
        super(menuService);
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/sentinelqueryfiles/filter/{idfilter}")
    public String vistapaginafiltro(@RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size,
                                    @PathVariable("idfilter") Integer id,
                              ModelMap interfazConPantalla){
        //Obetenemos el objeto Page del servicio
        Integer pagina = 0;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 10;
        if (size.isPresent()) {
            maxelementos = size.get();
        }

        Page<SentinelQueryFilesDto> dtoPage =
                this.service.buscarTodosPorFiltroId(PageRequest.of(pagina, maxelementos),id);
        interfazConPantalla.addAttribute("filtro_id",id);
        interfazConPantalla.addAttribute(pageNumbersAttributeKey, dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);

        return "sentinelqueryfiles/listapagina";
    }

    @GetMapping("/sentinelqueryfiles")
    public String vistapagina(@RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              ModelMap interfazConPantalla){

        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(userId);
        //Obetenemos el objeto Page del servicio
        Integer pagina = 0;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 10;
        if (size.isPresent()) {
            maxelementos = size.get();
        }

        Page<SentinelQueryFilesDto> dtoPage =
                this.service.buscarTodos(PageRequest.of(pagina, maxelementos));
        interfazConPantalla.addAttribute(pageNumbersAttributeKey, dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);

        return "sentinelqueryfiles/listapagina";
    }


    @PostMapping("/sentinelqueryfiles/{idusr}/delete")
    public String eliminarDatos(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<SentinelQueryFilesDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/sentinelqueryfiles";
        } else{
            //Mostrar página usuario no existe
            return "sentinelqueryfiles/detallesnoencontrado";
        }
    }




}
