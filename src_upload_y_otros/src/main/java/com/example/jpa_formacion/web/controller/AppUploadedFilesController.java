package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.UploadedFilesContentDto;
import com.example.jpa_formacion.dto.UploadedFilesDto;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.UploadedFilesService;
import com.example.jpa_formacion.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AppUploadedFilesController extends AbstractController <UploadedFilesDto> {

    private final UploadedFilesService service;
    private final UsuarioService usuarioService;


    public AppUploadedFilesController(MenuService menuService, UploadedFilesService service, UsuarioService usuarioService) {
        super(menuService);
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/uploadedfiles")
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
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();

        Page<UploadedFilesDto> dtoPage =
                this.service.buscarTodosPorUsuarioId(PageRequest.of(pagina,maxelementos),userId);
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);
        return "upload/listauploadpagina";
    }

    @GetMapping("/uploadedfiles/view/{id}")
    public String vistaDatosGrupo(@PathVariable("id") Integer id, ModelMap interfazConPantalla) throws IOException {
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UploadedFilesDto> optdto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (optdto.isPresent()){
            //Como encontré datos, leo el csv
            BufferedReader br = new BufferedReader(new FileReader(optdto.get().getPath()+"/"+ optdto.get().getDescription()));
            List<UploadedFilesContentDto> csvitemslist = new ArrayList<>();
            String line = br.readLine();
            String splitBy = ",";
            while ((line = br.readLine()) != null) {
                //If line is empty nothing is done
                if ( line.length() > 5){
                    // split on comma(',')
                    String[] datosCsv = line.split(splitBy);
                    // create car object to store values
                    UploadedFilesContentDto dto = new UploadedFilesContentDto();
                    // add values from csv to car object
                    dto.setLongitude(datosCsv[0]);
                    dto.setLatitude(datosCsv[1]);
                    dto.setSoc(datosCsv[2]);
                    dto.setVal1(datosCsv[3]);
                    dto.setVal2(datosCsv[4]);
                    dto.setVal3(datosCsv[5]);
                    dto.setDate(datosCsv[6]);
                    // adding car objects to a list
                    csvitemslist.add(dto);
                }

            }
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("lista",csvitemslist);

            return "upload/view";
        } else{
            //Mostrar página usuario no existe
            return "detallesnoencontrado";
        }
    }


    @PostMapping("/uploadedfiles/{idusr}/delete")
    public String eliminarDatosGrupo(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UploadedFilesDto> grupoDto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (grupoDto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/upload";
        } else{
            //Mostrar página usuario no existe
            return "detallesnoencontrado";
        }
    }


    //Postmaping para guardar
    @PostMapping("/uploadedfiles/{idusr}")
    public String guardarEdicionDatosGrupo(@PathVariable("idusr") Integer id, UploadedFilesDto dtoEntrada) throws Exception {
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UploadedFilesDto> dtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            dtoEntrada.setId(id);
            this.service.guardar(dtoEntrada);

            return String.format("redirect:/upload/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "detallesnoencontrado";
        }
    }

}
