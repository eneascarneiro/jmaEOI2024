package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.EvalScriptLaunchDto;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.EvalScriptService;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AppEvalScriptController extends AbstractController <EvalScriptDto> {

    private final EvalScriptService service;

    private final UsuarioService usuarioService;


    public AppEvalScriptController(MenuService menuService, EvalScriptService service, UsuarioService usuarioService) {
        super(menuService);
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/evalscript")
    public String vistapagina(@RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                    ModelMap interfazConPantalla){

        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Obetenemos el objeto Page del servicio
        Integer pagina = 0;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 5;
        if (size.isPresent()) {
            maxelementos = size.get();
        }

        Page<EvalScriptDto> dtoPage =
                this.service.buscarTodosPorUsuarioId(PageRequest.of(pagina,maxelementos),userId);
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);
        return "evalscript/listapagina";
    }

    @GetMapping("/evalscript/{idusr}")
    public String vistaDatos(@PathVariable("idusr") Integer id, ModelMap interfazConPantalla){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScriptDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            //Como encontré datos, obtengo el objerto de tipo "UsuarioDto"
            //addAttribute y thymeleaf no  entienden Optional
            EvalScriptDto attr = dto.get();
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("datos",attr);

            return "evalscript/edit";
        } else{
            //Mostrar página usuario no existe
            return "evalscript/detallesnoencontrado";
        }
    }


    @PostMapping("/evalscript/{idusr}/delete")
    public String eliminarDatos(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScriptDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/evalscript";
        } else{
            //Mostrar página usuario no existe
            return "evalscript/detallesnoencontrado";
        }
    }

    //Postmaping para guardar
    @PostMapping("/evalscript/{idusr}")
    public String guardarEdicionDatos(@PathVariable("idusr") Integer id, EvalScriptDto dtoEntrada) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(userId);
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScriptDto> dtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            dtoEntrada.setId(id);
            dtoEntrada.setUsuarioScript(usuario.get());
            this.service.guardar(dtoEntrada);

            return String.format("redirect:/evalscript/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "evalscript/detallesnoencontrado";
        }
    }


    @GetMapping("/evalscript/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final EvalScriptDto dto = new EvalScriptDto();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datos", dto);
        return "evalscript/edit";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/evalscript/registro")
    public String guardar( @ModelAttribute(name ="datos") EvalScriptDto dto) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(userId);
        //Comprobamos el patron
        dto.setUsuarioScript(usuario.get());
        EvalScriptDto dto1 = this.service.guardar(dto);
        return String.format("redirect:/evalscript/%s", dto1.getId());
    }

    @GetMapping("/evalscript/launch/{idevalscript}")
    public String vistaRegistroLaunch(@PathVariable("idevalscript") Integer id_script,Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        Optional<EvalScriptDto> evalScriptDto = this.service.encuentraPorId(id_script);
        EvalScriptLaunchDto evalScriptLaunchDto = new EvalScriptLaunchDto();
        if (evalScriptDto.isPresent()){
            //Mediante "addAttribute" comparto con la pantalla
            interfazConPantalla.addAttribute("datosscript", evalScriptDto.get());
            interfazConPantalla.addAttribute("datos", evalScriptLaunchDto);
            return "evalscript/launch";
        } else {
            //Mostrar página usuario no existe
            return "evalscript/detallesnoencontrado";
        }
    }

}
