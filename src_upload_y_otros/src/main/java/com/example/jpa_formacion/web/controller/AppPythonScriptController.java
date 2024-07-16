package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.EvalScriptLaunchDto;
import com.example.jpa_formacion.dto.PythonScriptDto;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.PythonScriptService;
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
public class AppPythonScriptController extends AbstractController <PythonScriptDto> {

    private final PythonScriptService service;

    private final UsuarioService usuarioService;


    public AppPythonScriptController(MenuService menuService, PythonScriptService service, UsuarioService usuarioService) {
        super(menuService);
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/pythonscript")
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

        Page<PythonScriptDto> dtoPage =
                this.service.buscarTodosPorUsuarioId(PageRequest.of(pagina,maxelementos),userId);
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);
        return "pythonscript/listapagina";
    }

    @GetMapping("/pythonscript/{id}")
    public String vistaDatos(@PathVariable("id") Integer id, ModelMap interfazConPantalla){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<PythonScriptDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            //Como encontré datos, obtengo el objerto de tipo "UsuarioDto"
            //addAttribute y thymeleaf no  entienden Optional
            PythonScriptDto attr = dto.get();
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("datos",attr);

            return "pythonscript/edit";
        } else{
            //Mostrar página usuario no existe
            return "pythonscript/detallesnoencontrado";
        }
    }


    @PostMapping("/pythonscript/{idusr}/delete")
    public String eliminarDatos(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<PythonScriptDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/pythonscript";
        } else{
            //Mostrar página usuario no existe
            return "pythonscript/detallesnoencontrado";
        }
    }

    //Postmaping para guardar
    @PostMapping("/pythonscript/{idusr}")
    public String guardarEdicionDatos(@PathVariable("idusr") Integer id, PythonScriptDto dtoEntrada) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(userId);
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<PythonScriptDto> dtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            dtoEntrada.setId(id);
            dtoEntrada.setUsuarioPythonScript(usuario.get());
            this.service.guardar(dtoEntrada);

            return String.format("redirect:/pythonscript/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "pythonscript/detallesnoencontrado";
        }
    }


    @GetMapping("/pythonscript/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final PythonScriptDto dto = new PythonScriptDto();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datos", dto);
        return "pythonscript/edit";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/PythonScriptDto/registro")
    public String guardar( @ModelAttribute(name ="datos") PythonScriptDto dto) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(userId);
        //Comprobamos el patron
        dto.setUsuarioPythonScript(usuario.get());
        PythonScriptDto dto1 = this.service.guardar(dto);
        return String.format("redirect:/pythonscript/%s", dto1.getId());
    }

    @GetMapping("/PythonScriptDto/launch/{idevalscript}")
    public String vistaRegistroLaunch(@PathVariable("idevalscript") Integer id_script,Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        Optional<PythonScriptDto> pythonScriptDto = this.service.encuentraPorId(id_script);
        EvalScriptLaunchDto evalScriptLaunchDto = new EvalScriptLaunchDto();
        if (pythonScriptDto.isPresent()){
            //Mediante "addAttribute" comparto con la pantalla
            interfazConPantalla.addAttribute("datosscript", pythonScriptDto.get());
            interfazConPantalla.addAttribute("datos", evalScriptLaunchDto);
            return "PythonScriptDto/launch";
        } else {
            //Mostrar página usuario no existe
            return "PythonScriptDto/detallesnoencontrado";
        }
    }

}
