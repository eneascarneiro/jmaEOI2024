package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.ConfiguationProperties;
import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.EvalScriptDto;
import com.example.jpa_formacion.dto.EvalScriptLaunchDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesTiffDto;
import com.example.jpa_formacion.model.EvalScript;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.EvalScriptLaunchService;
import com.example.jpa_formacion.service.EvalScriptService;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.UsuarioService;
import io.restassured.internal.util.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AppEvalScriptLaunchController extends AbstractController <EvalScriptLaunchDto> {

    private final EvalScriptLaunchService service;

    private final EvalScriptService evalScriptService;


    private ConfiguationProperties configuationProperties;


    public AppEvalScriptLaunchController(MenuService menuService, EvalScriptLaunchService service,
                                         EvalScriptService evalScriptService, ConfiguationProperties configuationProperties) {
        super(menuService);
        this.service = service;
        this.evalScriptService = evalScriptService;
        this.configuationProperties = configuationProperties;
    }

    @GetMapping("/evalscriptlaunch")
    public String vistapagina(@RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                    ModelMap interfazConPantalla){


        //Obetenemos el objeto Page del servicio
        Integer pagina = 0;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 5;
        if (size.isPresent()) {
            maxelementos = size.get();
        }

        Page<EvalScriptLaunchDto> dtoPage =
                this.service.buscarTodos(PageRequest.of(pagina,maxelementos));
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(dtoPage));
        interfazConPantalla.addAttribute("lista", dtoPage);
        return "evalscriptlaunch/listapagina";
    }

    @GetMapping("/evalscriptlaunch/{idusr}")
    public String vistaDatos(@PathVariable("idusr") Integer id, ModelMap interfazConPantalla){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScriptLaunchDto> dto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dto.isPresent()){
            //Como encontré datos, obtengo el objerto de tipo "UsuarioDto"
            //addAttribute y thymeleaf no  entienden Optional
            EvalScriptLaunchDto attr = dto.get();
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("datos",attr);

            return "evalscriptlaunch/edit";
        } else{
            //Mostrar página usuario no existe
            return "evalscriptlaunch/detallesnoencontrado";
        }
    }


    //Postmaping para guardar
    @PostMapping("/evalscriptlaunch/{idusr}")
    public String guardarEdicionDatos(@PathVariable("idusr") Integer id, EvalScriptLaunchDto dtoEntrada) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScript> evalScript = this.evalScriptService.encuentraPorIdEntity(userId);
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<EvalScriptLaunchDto> dtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (dtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            dtoEntrada.setId(id);
            dtoEntrada.setEvalScript(evalScript.get());
            this.service.guardar(dtoEntrada);

            return String.format("redirect:/evalscriptlaunch/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "evalscriptlaunch/detallesnoencontrado";
        }
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/evalscriptlaunch/show/{idfile}")
    public String showFile(@PathVariable("idfile") Integer id,
                           Model interfazConPantalla) throws Exception {

        Optional<EvalScriptLaunchDto> evalScriptLaunchDto = service.encuentraPorId(id);
        String pathcompleto =  evalScriptLaunchDto.get().getPathtiff();

        String path = pathcompleto.substring(0,pathcompleto.lastIndexOf("/"));
        String filename = pathcompleto.substring(pathcompleto.lastIndexOf("/")+1,pathcompleto.length());
        String nombredirinterno =  pathcompleto.substring(pathcompleto.lastIndexOf("userfiles/")+10,(pathcompleto.lastIndexOf("/")));
        //String uploadDir = "src/main/resources/static/files/src_user_data/sentinel/" + nombredirinterno ;
        String uploadDir = "/app/files/src_data_safe/" + nombredirinterno;
        System.out.println("Creando el directorio: " +uploadDir );
        //check if user folder exists if not create
        Files.createDirectories(Paths.get(uploadDir));
        System.out.println("directorio creado: " +uploadDir );

        if (evalScriptLaunchDto.isPresent()) {
            //componemos la url/api/descargartiff/
            String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/tiff/"+ nombredirinterno + "/"+ filename;

            URL url = new URL(urltext);


            InputStream is = url.openStream();
            String internalpath = uploadDir + "/response.tiff";
            OutputStream os = new FileOutputStream(uploadDir + "/response.tiff");

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();


            return String.format("redirect:/visor/image/evalscript/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "sentinelqueryfilestiff/detallesnoencontrado";
        }
    }
}
