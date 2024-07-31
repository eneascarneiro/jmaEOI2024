package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.dto.GrupoTrabajoDto;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.service.GrupoService;
import com.example.jpa_formacion.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class AppDemoGeneralController extends AbstractController <GrupoTrabajoDto> {

    private final GrupoService service;


    public AppDemoGeneralController(MenuService menuService, GrupoService service) {
        super(menuService);
        this.service = service;
    }
    @GetMapping("/demogeneral/ejemplomenus")
    public String vistamenus(ModelMap interfazConPantalla){
        return "demogeneral/index";
    }
    @GetMapping("/demogeneral/crearcarrito")
    public String crearcarrito(ModelMap interfazConPantalla, HttpSession session){
        //Obtengo de la sesión el carrito
        List<GrupoTrabajo> list = new ArrayList<>();
        session.setAttribute("carrito",list);
        return "demogeneral/carrito1";
    }
    @GetMapping("/demogeneral/add1carrito")
    public String add1carrito(ModelMap interfazConPantalla, HttpSession session){
        //Obtengo de la sesión el carrito
        List<GrupoTrabajo> list = (List<GrupoTrabajo>) session.getAttribute("carrito");
        //Cuantos elementos tengo??
        Integer num_elem = list.size();
        interfazConPantalla.addAttribute("num_antes",num_elem);
        //Añado uno
        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);
        GrupoTrabajo grupoTrabajo = new GrupoTrabajo();
        grupoTrabajo.setId(rand_int1);
        grupoTrabajo.setDescripcion("prueba:" +  rand_int1);
        grupoTrabajo.setActive(true);
        list.add(grupoTrabajo);
        //Elementos despues
        Integer num_elem_despues = list.size();
        interfazConPantalla.addAttribute("num_elem_despues",num_elem_despues);
        //Devuelvo el dato a la sesion
        session.setAttribute("carrito",list);
        interfazConPantalla.addAttribute("carrito",list);
        return "demogeneral/carrito2";
    }
    @PostMapping("/demogeneral/eliminar/{index}")
    public String eliminarelem(@PathVariable("index") Integer index,ModelMap interfazConPantalla, HttpSession session){
        //Obtengo de la sesión el carrito
        List<GrupoTrabajo> list = (List<GrupoTrabajo>) session.getAttribute("carrito");
        //Cuantos elementos tengo??
        Integer num_elem = list.size();
        //Elimino el elemento
        GrupoTrabajo grupoTrabajo = list.get(index);
        System.out.println(grupoTrabajo.getId());
        //list.remove(index);
        //List<GrupoTrabajo> list1 = new ArrayList<GrupoTrabajo>();
        //GrupoTrabajo grupoTrabajo1 = list1.get(index);
        //System.out.println(grupoTrabajo1.getId());
        List<GrupoTrabajo> list2 = new ArrayList<GrupoTrabajo>();
        for (int j = 0; j < list.size(); j++) {
            System.out.println(j);
            if (j == index)
                System.out.println("elenento no utilizado:" + j );
            else {
                list2.add(list.get(j));
            }
        }
        //Guadar carrito en la sesion
        session.removeAttribute("carrito");
        session.setAttribute("carrito",list2);
        //Elementos despues
        Integer num_elem_despues = list2.size();
        interfazConPantalla.addAttribute("num_elem",num_elem);
        interfazConPantalla.addAttribute("num_elem_despues",num_elem_despues);
        interfazConPantalla.addAttribute("carrito",list2);
        return "redirect:/demogeneral/add1carrito";
    }

    @GetMapping("/demogeneral")
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
        return "demogeneral/listagrupospagina";
    }

    @GetMapping("/demogeneral/{idusr}")
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

            return "demogeneral/edit";
        } else{
            //Mostrar página usuario no existe
            return "demogeneral/detallesgruponoencontrado";
        }
    }


    @PostMapping("/demogeneral/{idusr}/delete")
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
            return "demogeneral/detallesgruponoencontrado";
        }
    }
    @PostMapping("/demogeneral/{idusr}/habilitar")
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
            return "redirect:/demogeneral";
        } else{
            //Mostrar página usuario no existe
            return "demogeneral/detallesgruponoencontrado";
        }
    }

    //Postmaping para guardar
    @PostMapping("/demogeneral/{idusr}")
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

            return String.format("redirect:/demogeneral/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "demogeneral/detallesgruponoencontrado";
        }
    }

    //Para crear un grupo hay dos bloques
    //El que genera la pantalla para pedir los datos de tipo GetMapping
    //Cuando pasamos informacion a la pantalla hay que usar ModelMap
    @GetMapping("/demogeneral/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final GrupoTrabajoDto grupoTrabajoDto = new GrupoTrabajoDto();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datos", grupoTrabajoDto);
        return "demogeneral/registro";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/demogeneral/registro")
    public String guardarUsuario( @ModelAttribute(name ="datosUsuario") GrupoTrabajoDto gruposDto) throws Exception {
        //Comprobamos el patron
        GrupoTrabajoDto grupoTrabajoDto1 = this.service.guardar(gruposDto);
        return String.format("redirect:/demogeneral/%s", grupoTrabajoDto1.getId());
    }
}
