package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.apiitem.*;
import com.example.jpa_formacion.config.ConfiguationProperties;
import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.*;
import com.example.jpa_formacion.model.*;
import com.example.jpa_formacion.service.*;
import com.example.jpa_formacion.util.CsvGeneratorUtil;
import com.example.jpa_formacion.util.Request;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class AppCallApiSenc4farming extends AbstractController <GrupoTrabajoDto> {
    @Autowired
    private CsvGeneratorUtil csvGeneratorUtil;

    private final DatosLucas2018Service datosLucas2018Service;
    private final FiltroListarArchivosService filtroListarArchivosService;
    private final SentinelQueryFilesService sentinelQueryFilesService;

    private final SentinelQueryFilesTiffService sentinelQueryFilesTiffService;
    private final EvalScriptService evalScriptService;
    private final EvalScriptLaunchService evalScriptLaunchService;
    private  List<Listfiles> objlistfiles;

    private  List<Listfilestiff> objlistfilestiff;

    private  List<ListfilesEvalScript> objlistfilesevalscript;

    private  List<DatosLucas2018Api> objlistdatoslucas2018Api;

    private  List<UploadedFilesReflectance> objListUploadedFilesReflectances;
    private  final UsuarioService  usuarioService;

    private final UploadedFilesService uploadedFilesService;
    @Autowired
    private Environment environment;
    private final ConfiguationProperties configuationProperties;

    public AppCallApiSenc4farming(MenuService menuService,
                                  DatosLucas2018Service datosLucas2018Service,
                                  FiltroListarArchivosService filtroListarArchivosService,
                                  SentinelQueryFilesService sentinelQueryFilesService,
                                  SentinelQueryFilesTiffService sentinelQueryFilesTiffService,
                                  EvalScriptService evalScriptService, EvalScriptLaunchService evalScriptLaunchService,
                                  List<Listfilestiff> objlistfilestiff,
                                  UsuarioService usuarioService,
                                  UploadedFilesService uploadedFilesService, ConfiguationProperties configuationProperties) {
        super(menuService);
        this.datosLucas2018Service = datosLucas2018Service;
        this.filtroListarArchivosService = filtroListarArchivosService;
        this.sentinelQueryFilesService = sentinelQueryFilesService;
        this.sentinelQueryFilesTiffService = sentinelQueryFilesTiffService;
        this.evalScriptService = evalScriptService;
        this.evalScriptLaunchService = evalScriptLaunchService;
        this.objlistfilestiff = objlistfilestiff;
        this.uploadedFilesService = uploadedFilesService;
        this.configuationProperties = configuationProperties;
        this.objlistfiles = new ArrayList<>();
        this.objlistfilesevalscript = new ArrayList<>();
        this.objListUploadedFilesReflectances = new ArrayList<>();
        this.objlistdatoslucas2018Api = new ArrayList<>();
        this.usuarioService = usuarioService;
    }
    @GetMapping("/api/downloadtiff")
    public String downloadtiff(Model interfazConPantalla){
        //Listado de los EvalScript del usuario
        List<EvalScript> evalScripts = evalScriptService.buscarEntidades();
        //Instancia en memoria del dto a informar en la pantalla
        DownloadtiffDto downloadtiffDto = new DownloadtiffDto();
        downloadtiffDto.setFile("S2A_MSIL2A_20230901T000231_N0509_R030_T56JPT_20230901T035601.SAFE");
        downloadtiffDto.setEvalScripts(evalScripts);
        //Obtenemos los credenciales de sesión
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datos",downloadtiffDto);
        System.out.println("Preparando pantalla de busqueda");
        return "evalscript/descargartiff";
    }

    @GetMapping("/api/credentials")
    public String getcredentials(  Model interfazConPantalla, HttpSession session){
        //Instancia en memoria del dto a informar en la pantalla
        Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
        //Check if credentials exists
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token= "access_token";

        Object cliId = session.getAttribute(sessionKeycliid);
        Object secret = session.getAttribute(sessionKeySecret);
        Object token =  session.getAttribute(sessionAccess_token);
        System.out.println("En GetMapping /api/credentials");
        if (cliId != null){
            System.out.println("En GetMapping /api/credentials: tenemos datos en sesión");
            copernicuscredentials.setClientid((String) cliId);
            copernicuscredentials.setSecret((String) secret);
            copernicuscredentials.setToken((String) token);
        }

        //Obtenemos los credenciales de sesión
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("consulta",copernicuscredentials);
        return "api/credentials";
    }
    @PostMapping("/api/credentials")
    public String getcredentials(@ModelAttribute(name ="consulta") Copernicuscredentials copernicuscredentials
            , HttpSession session, Model interfazConPantalla) {
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token= "access_token";

        String defclientid = "sh-ef626da0-f7d8-40aa-a75d-9f84cac9871d";
        String defsecret = "2TyNYdw6t6U0gJjHl1v3kjQoKdy9mHVW";
        System.out.println("En Postmapping /api/credentials: tenemos datos en sesión:" + copernicuscredentials.getClientid());
        //session.setAttribute(sessionKeycliid,defclientid );
        //session.setAttribute(sessionKeySecret,defsecret);
        session.setAttribute(sessionKeycliid,copernicuscredentials.getClientid() );
        session.setAttribute(sessionKeySecret,copernicuscredentials.getSecret());
        //llamamos a pedir el token al api

        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/sentinel/gettoken/";
        //Obtenemos los datos del usuario de la sesión
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(superCustomerUserDetails.getUsername());

        //Obtenemos el id del usuario y el grupo

        JSONObject requestParam = new JSONObject();
        requestParam.put("client_id",copernicuscredentials.getClientid());
        requestParam.put("client_secret",copernicuscredentials.getSecret());
        //Comprobamos el patron
        //Se invoca a la URL

        System.out.println("Post pantalla de busqueda 50");
        Request request1 = new Request(urltext, requestParam);
        //Obtenemos el token del request
        try {
            String jsonArray = request1.output;
            System.out.println("Post pantalla de busqueda json leido");
            System.out.println(jsonArray);

            JSONObject jsonObject = new JSONObject(jsonArray);
            if (jsonObject.has("errorcode")) {
                String text = "Credential error: " + jsonObject.get("errormessage");
                interfazConPantalla.addAttribute("errormessage",text);
                return "api/credentials";
            }
            else{
                copernicuscredentials.setToken(jsonObject.get(sessionAccess_token).toString());
                session.setAttribute(sessionAccess_token,jsonObject.get(sessionAccess_token).toString());
                System.out.println(copernicuscredentials.getToken());
                //Obtenemos los credenciales de sesión
                //Mediante "addAttribute" comparto con la pantalla
                return "index";
            }

        } catch (JSONException err) {
            System.out.println("Error: " + err);
            String text = "Credential error: " + err.getMessage() + ", contact administrator";
            interfazConPantalla.addAttribute("errormessage",text);
            return "api/credentials";
        }



    }

    @GetMapping("/api/listfiles")
    public String listfilesGet(Model interfazConPantalla, HttpSession session){
        //Instancia en memoria del dto a informar en la pantalla
        System.out.println("En /api/listfiles get");
        ListarArchivosDto listarArchivosDto = new ListarArchivosDto();
        //Instancia en memoria del dto a informar en la pantalla
        Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
        //Check if credentials exists
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token= "access_token";


        Object cliId = session.getAttribute(sessionKeycliid);
        Object secret = session.getAttribute(sessionKeySecret);
        Object token =  session.getAttribute(sessionAccess_token);


        if (cliId != null) {
            interfazConPantalla.addAttribute("consulta", listarArchivosDto);
            copernicuscredentials.setClientid((String) cliId);
            copernicuscredentials.setSecret((String) secret);
            copernicuscredentials.setToken((String) token);
            return "api/odadaconsulta";
        }
        else{
            interfazConPantalla.addAttribute("consulta",copernicuscredentials);
            return "api/credentials";
        }

    }

    @GetMapping("/api/listfiles/{id}")
    public String listfilesGetId(Model interfazConPantalla,@PathVariable("id") Integer id, HttpSession session){
        //Con el id del filtro obtenemos el filtro
        Optional<FiltroListarArchivos> listarArchivos = filtroListarArchivosService.buscar(id);
        if ( listarArchivos.isPresent()){
            //Instancia en memoria del dto a informar en la pantalla
            ListarArchivosDto listarArchivosDto = new ListarArchivosDto();
            listarArchivosDto.setFilterid(id);
            listarArchivosDto.setPolygon(listarArchivos.get().getPolygon());
            listarArchivosDto.setDateIni(listarArchivos.get().getDateIni());
            listarArchivosDto.setDateFin(listarArchivos.get().getDateFin());
            listarArchivosDto.setReference(listarArchivos.get().getReference());
            listarArchivosDto.setCloudCover(listarArchivos.get().getCloudCover());
            listarArchivosDto.setUserid(Long.valueOf(((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID()));
            listarArchivosDto.setUserName(((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            Optional<Usuario> usuario = usuarioService.buscar(((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID());
            if (usuario.isPresent())
            {
                listarArchivosDto.setGroupid(usuario.get().getGrupoTrabajo().getId());
            }
            //Instancia en memoria del dto a informar en la pantalla
            Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
            //Check if credentials exists
            String sessionKeycliid = "clienteid";
            Object cliId = session.getAttribute(sessionKeycliid);
            if (cliId != null){
                interfazConPantalla.addAttribute("consulta",listarArchivosDto);
                return "api/odadaconsulta";
            }
            else{
                interfazConPantalla.addAttribute("consulta",copernicuscredentials);
                return "redirect:/api/credentials";
            }
        }
        else {
            return "api/odadaconsultanoencontrado";
        }


    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/api/listfiles")
    public String listfilesPost( @ModelAttribute(name ="consulta") ListarArchivosDto listarArchivosDto,
                                 Model interfazConPantalla)  {

        System.out.println("En /api/listfiles post");
        //Objeto para guardar el filtro de la consulta
        FiltroListarArchivosDto filtroListarArchivosDto = new FiltroListarArchivosDto();
        //componemos la url

        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/generarlista2ANew/";

        //Obtenemos los datos del usuario de la sesión
        String userName = "no informado";
        System.out.println("Post pantalla de busqueda 21");
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(superCustomerUserDetails.getUsername());
        System.out.println("Post pantalla de busqueda 22");
        //Comprobamos si hay usuario logeado
        if (superCustomerUserDetails.getUsername().equals("anonimo@anonimo")) {
            listarArchivosDto.setUserName(superCustomerUserDetails.getUsername());
            listarArchivosDto.setGroupid(0L);
            listarArchivosDto.setUserid(0L);
        } else {
            System.out.println("Post pantalla de busqueda 30");
            userName = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Optional<Usuario> usuario = usuarioService.getRepo().findUsuarioByEmailAndActiveTrue(userName);

            if (usuario.isPresent()) {
                listarArchivosDto.setUserName(usuario.get().getEmail());
                listarArchivosDto.setGroupid(usuario.get().getGrupoTrabajo().getId());
                listarArchivosDto.setUserid(usuario.get().getId());
            } else {
                listarArchivosDto.setUserName("anonimo@anonimo");
                listarArchivosDto.setGroupid(0L);
                listarArchivosDto.setUserid(0L);
            }
        }
        System.out.println("Post pantalla de busqueda 40");
        //Obtenemos el id del usuario y el grupo
        FiltroListarArchivosDto filtroListarArchivosdto = null;
        try {
            JSONObject requestParam = new JSONObject();
            requestParam.put("userName", listarArchivosDto.getUserName());
            requestParam.put("groupid", listarArchivosDto.getGroupid());
            requestParam.put("userid", listarArchivosDto.getUserid());
            requestParam.put("reference", listarArchivosDto.getReference());
            requestParam.put("dateini", listarArchivosDto.getDateIni());
            requestParam.put("datefin", listarArchivosDto.getDateFin());
            requestParam.put("polygon", listarArchivosDto.getPolygon());
            requestParam.put("cloudcover", listarArchivosDto.getCloudCover());
            //Se guardan los datos para almacenar el filtro de la consulta
            filtroListarArchivosDto.setReference(listarArchivosDto.getReference());
            filtroListarArchivosDto.setPolygon(listarArchivosDto.getPolygon());
            filtroListarArchivosDto.setCloudCover(listarArchivosDto.getCloudCover());
            filtroListarArchivosDto.setDateIni(listarArchivosDto.getDateIni());
            filtroListarArchivosDto.setDateFin(listarArchivosDto.getDateFin());
            filtroListarArchivosDto.setNunberOfResults(0);
            //Con el id tengo que buscar el registro a nivel de entidad
            Optional<Usuario> usuario = this.usuarioService.encuentraPorIdEntity(Math.toIntExact(listarArchivosDto.getUserid()));
            filtroListarArchivosDto.setUsuariofiltro(usuario.get());
            //Comprobamos el patron
            //Se invoca a la URL
            System.out.println("Post pantalla de busqueda 50");
            Request request1 = new Request(urltext, requestParam);
            System.out.println("Post pantalla de busqueda llamada realizada");
            String jsonArray = request1.output;
            System.out.println("Post pantalla de busqueda json leido");
            System.out.println(jsonArray);
            try {
                JSONObject jsonObject = new JSONObject(jsonArray);
                Iterator<String> keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (jsonObject.get(key) instanceof JSONObject) {
                        // do something with jsonObject here
                        JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                        Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                        while (keysint.hasNext()) {
                            String keyint = keysint.next();
                            System.out.println("valores para el json");
                            System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                    jsonObject1.get(keyint).toString());
                            Listfiles itemlistfilesCheck = findfilerecord(keyint);
                            System.out.println("qqqqqqqq");
                            if (itemlistfilesCheck.getKey().equals(-1)) {
                                System.out.println("valores para el json no existe indice");
                                Listfiles itmlistfilesnew = new Listfiles();
                                Setlistfilesfield(key, jsonObject1, keyint, itmlistfilesnew, 0);
                            } else {
                                // You use this ".get()" method to actually get your Listfiles from the Optional object
                                System.out.println(" /api/listfiles valores para el json existe indice");

                                objlistfiles.remove(itemlistfilesCheck);
                                Setlistfilesfield(key, jsonObject1, keyint, itemlistfilesCheck, 1);
                            }
                        }
                    }
                }
                filtroListarArchivosDto.setNunberOfResults(objlistfiles.size());
            } catch (JSONException err) {
                System.out.println("Error: " + err.toString());
            }
            //Eliminamos los resultados de la consulta anterior por referencia
            System.out.println("Antes de eliminar datos anteriores id:" + listarArchivosDto.getFilterid() );
            //sentinelQueryFilesTiffService.getRepo().deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_FiltroListarArchivos_Id();
            //sentinelQueryFilesService.deleteDesdeFiltro(listarArchivosDto.getFilterid());


            System.out.println("Despues de eliminar datos anteriores id:" + listarArchivosDto.getFilterid() );

            //Guardamos los datos del filtro
            filtroListarArchivosDto.setId(listarArchivosDto.getFilterid());
            filtroListarArchivosdto = filtroListarArchivosService.guardar(filtroListarArchivosDto);
            System.out.println("request1.getResponseHeaders()");
            System.out.println(request1.getResponseHeaders());
            System.out.println("request1.output");
            System.out.println(request1.output);

            //Guardamos desde la lista
            sentinelQueryFilesService.guardarDesdeConsulta(objlistfiles,filtroListarArchivosdto.getId());
            interfazConPantalla.addAttribute("resultado", "");
            return String.format("redirect:/sentinelqueryfiles/filter/%s", filtroListarArchivosdto.getId());

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
            return String.format("redirect:/sentinelqueryfiles");
        }
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }

    private  Listfiles findfilerecord( String key){
        Listfiles listfilesini = new Listfiles();
        listfilesini.setKey(-1);
        for(Listfiles p : objlistfiles){
            if ( p.getKey().equals( Integer.parseInt(key)) ){
                listfilesini =  p;
            }
        }

        return listfilesini;
    }
    private void Setlistfilesfield(String key, JSONObject jsonObject1, String keyint, Listfiles listfilesItm
                                   , Integer option) {
        switch (key.toLowerCase()) {
            case "id":
                listfilesItm.setId(jsonObject1.get(keyint).toString());
                break;
            case "name":
                listfilesItm.setName(jsonObject1.get(keyint).toString());
                break;
            case "s3path":
                listfilesItm.setS3Path(jsonObject1.get(keyint).toString());
                break;
            case "modificationdate":
                listfilesItm.setModificationDate(jsonObject1.get(keyint).toString());
                break;
            case "online":
                listfilesItm.setOnline(jsonObject1.get(keyint).toString());
                break;
            case "origindate":
                listfilesItm.setOriginDate(jsonObject1.get(keyint).toString());
                break;
            case "publicationdate":
                listfilesItm.setPublicationDate(jsonObject1.get(keyint).toString());
                break;
            case "reference":
                listfilesItm.setReference(jsonObject1.get(keyint).toString());
                break;
            case "userid":
                listfilesItm.setUserid(jsonObject1.get(keyint).toString());
                break;
            case "groupid":
                listfilesItm.setGroupid(jsonObject1.get(keyint).toString());
                break;
            case "footprint":
                listfilesItm.setFootprint(jsonObject1.get(keyint).toString());
                break;
            case "geofootprint":
                listfilesItm.setGeofootprint(jsonObject1.get(keyint).toString());
                break;
        }
        if (option > 0){
            //el elemennto existe y se sustituye
            objlistfiles.add(listfilesItm);

        }else {
            //el elemennto no existe y se añadeç
            listfilesItm.setKey( Integer.parseInt(keyint));
            objlistfiles.add(listfilesItm);
        }

    }

    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/api/listfiles/downloadbands/{idquery}")
    public String listfilesDownloadbands(@PathVariable("idquery") Integer id, Model interfazConPantalla,HttpSession session) throws Exception {
        //Objeto para guardar el filtro de la consulta
        Optional<SentinelQueryFiles> sentinelQueryFiles = sentinelQueryFilesService.getRepo().findById(id);
        SentinelQueryFiles sentinelQueryFiles1 = new SentinelQueryFiles();
        if (sentinelQueryFiles.isPresent()) {
            sentinelQueryFiles1 = sentinelQueryFiles.get();
        }
        //componemos la url
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/sentinel/decargartiffbandas/";
        //Obtenemos los datos del usuario de la sesión
        String userName = "no informado";
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(superCustomerUserDetails.getUsername());

        //Instancia en memoria del dto a informar en la pantalla
        Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
        //Check if credentials exists
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token = "access_token";
        Object cliId = session.getAttribute(sessionKeycliid);
        Object secret = session.getAttribute(sessionKeySecret);
        Object token = session.getAttribute(sessionAccess_token);
        System.out.println("Encontré el token en la sesión");
        System.out.println((String) token);
        if (cliId != null) {
            copernicuscredentials.setClientid((String) cliId);
            copernicuscredentials.setSecret((String) secret);
            copernicuscredentials.setToken((String) token);


            //Obtenemos el id del usuario y el grupo
            FiltroListarArchivosDto filtroListarArchivosdto = null;
            try {
                JSONObject requestParam = new JSONObject();
                requestParam.put("userid", superCustomerUserDetails.getUserID());
                requestParam.put("queryid", sentinelQueryFiles.get().getId());
                requestParam.put("offset", 0.1);
                requestParam.put("reference", sentinelQueryFiles.get().getFiltroListarArchivos().getReference());
                requestParam.put("date", sentinelQueryFiles.get().getPublicationDate());
                requestParam.put("polygon", sentinelQueryFiles.get().getFiltroListarArchivos().getPolygon());
                requestParam.put("geofootprint", sentinelQueryFiles.get().getGeofootprint());
                requestParam.put("cloudcover", sentinelQueryFiles.get().getFiltroListarArchivos().getCloudCover());
                requestParam.put("sentinelfilename", sentinelQueryFiles.get().getName());
                requestParam.put("sentinelfileid", sentinelQueryFiles.get().getSentinelId());
                requestParam.put("clienteid", copernicuscredentials.getClientid());
                requestParam.put("secret", copernicuscredentials.getSecret());
                requestParam.put("token", (String) token);
                //Comprobamos el patron
                //Se invoca a la URL
                System.out.println("listfilesDownloadbands: Post pantalla de busqueda 50");
                Request request1 = new Request(urltext, requestParam);
                System.out.println(request1.output);

                String jsonArray = request1.output;
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray);
                    Iterator<String> keys = jsonObject.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (jsonObject.get(key) instanceof JSONObject) {
                            // do something with jsonObject here
                            JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                            Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                            while (keysint.hasNext()) {
                                String keyint = keysint.next();
                                System.out.println("valores para el json");
                                System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                        jsonObject1.get(keyint).toString());
                                Listfilestiff itemlistfilesCheck = findfiletiffrecord(keyint);
                                System.out.println("qqqqqqqq");
                                if (itemlistfilesCheck.getKey().equals(-1)) {
                                    System.out.println("valores para el json no existe indice");
                                    Listfilestiff itmlistfilesnew = new Listfilestiff();
                                    Setlistfilestifffield(key, jsonObject1, keyint, itmlistfilesnew, 0);
                                } else {
                                    // You use this ".get()" method to actually get your Listfiles from the Optional object
                                    System.out.println(" /api/listfiles/downloadbands get valores para el json existe indice");

                                    objlistfilestiff.remove(itemlistfilesCheck);
                                    Setlistfilestifffield(key, jsonObject1, keyint, itemlistfilesCheck, 1);
                                }
                            }
                        }
                    }
                    if (objlistfilestiff.isEmpty())
                        sentinelQueryFiles1.setNunberOfTiff(0);
                    else
                        sentinelQueryFiles1.setNunberOfTiff(objlistfilestiff.size());
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }
                //Eliminamos los resultados de la consulta anterior por referencia
                System.out.println("sentinelQueryFilesTiffService borramos para el id de files");
                System.out.println(sentinelQueryFiles1.getId());
                sentinelQueryFilesTiffService.getRepo().deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_Id(sentinelQueryFiles1.getId());

                if (!objlistfilestiff.isEmpty()) {
                    //Guardamos los datos
                    sentinelQueryFilesService.getRepo().save(sentinelQueryFiles1);


                    //Guardamos desde la lista
                    sentinelQueryFilesTiffService.guardarDesdeConsulta(objlistfilestiff, sentinelQueryFiles1.getId());

                    return String.format("redirect:/sentinelqueryfiles/filter/" + sentinelQueryFiles.get().getId());

                } else {
                    //Mostrar página usuario no existe
                    return "sentinelqueryfiles/detallesnoencontrado";
                }
            } catch (Exception e) {
                System.out.println("Error Message");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
                return String.format("redirect:/sentinelqueryfiles");
            }
        } else {
            interfazConPantalla.addAttribute("consulta", copernicuscredentials);
            return "redirect:/api/credentials";
        }
    }

    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @GetMapping ("/api/listfiles/downloadbands/{idquery}")
    public String listfilesDownloadbandsGet (@PathVariable("idquery") Integer id, Model interfazConPantalla,HttpSession session) throws Exception {
        //Objeto para guardar el filtro de la consulta
        Optional<SentinelQueryFiles> sentinelQueryFiles = sentinelQueryFilesService.getRepo().findById(id);
        SentinelQueryFiles sentinelQueryFiles1 = new SentinelQueryFiles();
        if (sentinelQueryFiles.isPresent()) {
            sentinelQueryFiles1 = sentinelQueryFiles.get();
        }
        //componemos la url
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/sentinel/decargartiffbandas/";
        //Obtenemos los datos del usuario de la sesión
        String userName = "no informado";
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(superCustomerUserDetails.getUsername());

        //Instancia en memoria del dto a informar en la pantalla
        Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
        //Check if credentials exists
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token= "access_token";
        Object cliId = session.getAttribute(sessionKeycliid);
        Object secret = session.getAttribute(sessionKeySecret);
        Object token =  session.getAttribute(sessionAccess_token);
        if (cliId != null) {
            copernicuscredentials.setClientid((String) cliId);
            copernicuscredentials.setSecret((String) secret);
            copernicuscredentials.setToken((String) token);


            //Obtenemos el id del usuario y el grupo
            FiltroListarArchivosDto filtroListarArchivosdto = null;
            try {
                JSONObject requestParam = new JSONObject();
                requestParam.put("userid", superCustomerUserDetails.getUserID());
                requestParam.put("queryid", sentinelQueryFiles.get().getId());
                requestParam.put("offset", 0.1);
                requestParam.put("reference", sentinelQueryFiles.get().getFiltroListarArchivos().getReference());
                requestParam.put("date", sentinelQueryFiles.get().getPublicationDate());
                requestParam.put("polygon", sentinelQueryFiles.get().getFiltroListarArchivos().getPolygon());
                requestParam.put("geofootprint", sentinelQueryFiles.get().getGeofootprint());
                requestParam.put("cloudcover", sentinelQueryFiles.get().getFiltroListarArchivos().getCloudCover());
                requestParam.put("sentinelfilename", sentinelQueryFiles.get().getName());
                requestParam.put("sentinelfileid", sentinelQueryFiles.get().getSentinelId());
                requestParam.put("clienteid",  copernicuscredentials.getClientid());
                requestParam.put("secret",  copernicuscredentials.getSecret());
                requestParam.put("token",  copernicuscredentials.getToken());
                //Comprobamos el patron
                //Se invoca a la URL
                System.out.println("Post pantalla de busqueda 50");
                Request request1 = new Request(urltext, requestParam);
                String jsonArray = request1.output;
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray);
                    Iterator<String> keys = jsonObject.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (jsonObject.get(key) instanceof JSONObject) {
                            // do something with jsonObject here
                            JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                            Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                            while (keysint.hasNext()) {
                                String keyint = keysint.next();
                                System.out.println("valores para el json");
                                System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                        jsonObject1.get(keyint).toString());
                                Listfilestiff itemlistfilesCheck = findfiletiffrecord(keyint);
                                System.out.println("qqqqqqqq");
                                if (itemlistfilesCheck.getKey().equals(-1)) {
                                    System.out.println("valores para el json no existe indice");
                                    Listfilestiff itmlistfilesnew = new Listfilestiff();
                                    Setlistfilestifffield(key, jsonObject1, keyint, itmlistfilesnew, 0);
                                } else {
                                    // You use this ".get()" method to actually get your Listfiles from the Optional object
                                    System.out.println(" /api/listfiles/downloadbands get valores para el json existe indice");

                                    objlistfilestiff.remove(itemlistfilesCheck);
                                    Setlistfilestifffield(key, jsonObject1, keyint, itemlistfilesCheck, 1);
                                }
                            }
                        }
                    }
                    if (objlistfilestiff.isEmpty())
                        sentinelQueryFiles1.setNunberOfTiff(0);
                    else
                        sentinelQueryFiles1.setNunberOfTiff(objlistfilestiff.size());
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }
                //Eliminamos los resultados de la consulta anterior por referencia
                System.out.println("Eliminando datos sentinelQueryFilesTiffService");
                sentinelQueryFilesTiffService.getRepo().deleteSentinelQueryFilesTiffBySentinelQueryFilesfortiff_Id(sentinelQueryFiles1.getId());

                if (!objlistfilestiff.isEmpty()) {
                    //Guardamos los datos
                    System.out.println("Eliminando datos sentinelQueryFilesService");
                    sentinelQueryFilesService.getRepo().save(sentinelQueryFiles1);


                    //Guardamos desde la lista
                    System.out.println("Eliminando datos sentinelQueryFilesTiffService");
                    sentinelQueryFilesTiffService.guardarDesdeConsulta(objlistfilestiff, sentinelQueryFiles1.getId());

                    return String.format("redirect:/sentinelqueryfiles/filter/" + sentinelQueryFiles.get().getId());

                } else {
                    //Mostrar página usuario no existe
                    return "sentinelqueryfiles/detallesnoencontrado";
                }
            } catch (Exception e) {
                System.out.println("Error Message");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
                return String.format("redirect:/sentinelqueryfiles");
            }
        } else{
            interfazConPantalla.addAttribute("consulta",copernicuscredentials);
            return "redirect:/api/credentials";
        }
    }

    private  Listfilestiff findfiletiffrecord( String key){
        Listfilestiff listfilesini = new Listfilestiff();
        listfilesini.setKey(-1);
        for(Listfilestiff p : objlistfilestiff){
            if ( p.getKey().equals( Integer.parseInt(key)) ){
                listfilesini =  p;
            }
        }

        return listfilesini;
    }
    private void Setlistfilestifffield(String key, JSONObject jsonObject1, String keyint, Listfilestiff listfilesItm
            , Integer option) {
        switch (key.toLowerCase()) {
            case "band":
                listfilesItm.setBand(jsonObject1.get(keyint).toString());
                break;
            case "path":
                listfilesItm.setPath(jsonObject1.get(keyint).toString());
                break;

        }
        if (option > 0){
            //el elemennto existe y se sustituye
            objlistfilestiff.add(listfilesItm);

        }else {
            //el elemennto no existe y se añadeç
            listfilesItm.setKey( Integer.parseInt(keyint));
            objlistfilestiff.add(listfilesItm);
        }

    }
    private void Setlistfilesevalscriptfield(String key, JSONObject jsonObject1, String keyint,
                 ListfilesEvalScript listfilesItm  , Integer option) {
        switch (key.toLowerCase()) {
            case "path":
                listfilesItm.setPath(jsonObject1.get(keyint).toString());
                break;

        }
        if (option > 0){
            //el elemennto existe y se sustituye
            objlistfilesevalscript.add(listfilesItm);

        }else {
            //el elemennto no existe y se añadeç
            listfilesItm.setKey( Integer.parseInt(keyint));
            objlistfilesevalscript.add(listfilesItm);
        }

    }

    /**
     *
     * @param key
     * @return
     */
    private  ListfilesEvalScript findfileevalscriptrecord( String key){
        ListfilesEvalScript listfilesini = new ListfilesEvalScript();
        listfilesini.setKey(-1);
        for (ListfilesEvalScript p : objlistfilesevalscript) {
            if (p.getKey().equals(Integer.parseInt(key))) {
                listfilesini = p;
            }
        }


        return listfilesini;
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/api/listfiles/executeevalscript")
    public String executeevalscript(@ModelAttribute(name ="datosscript") EvalScriptDto dtoEvalscript,
                                    @ModelAttribute(name ="datos") EvalScriptLaunchDto evalScriptLaunchDtoIn,
                                    Model interfazConPantalla,HttpSession session) throws Exception {

        //Obtenemos los datos del usuario de la sesión
        String userName = "no informado";
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Instancia en memoria del dto a informar en la pantalla
        Copernicuscredentials copernicuscredentials = new Copernicuscredentials();
        //Check if credentials exists
        String sessionKeycliid = "clienteid";
        String sessionKeySecret = "secret";
        String sessionAccess_token= "access_token";
        Object cliId = session.getAttribute(sessionKeycliid);
        Object secret = session.getAttribute(sessionKeySecret);
        Object token =  session.getAttribute(sessionAccess_token);
        if (cliId != null){
            copernicuscredentials.setClientid((String) cliId);
            copernicuscredentials.setSecret((String) secret);
            copernicuscredentials.setToken((String) token);
            //componemos la url
            String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/sentinel/executeevalscript/";
            //Obtenemos el id del usuario y el grupo
            evalScriptLaunchDtoIn.setEvalScript(evalScriptService.encuentraPorIdEntity(dtoEvalscript.getId()).get());
            EvalScriptLaunchDto  evalScriptLaunchDto = evalScriptLaunchService.guardar(evalScriptLaunchDtoIn);
            try {
                JSONObject requestParam = new JSONObject();
                requestParam.put("userid", superCustomerUserDetails.getUserID());
                requestParam.put("queryid", evalScriptLaunchDto.getId());
                requestParam.put("offset", evalScriptLaunchDto.getOfsset());
                requestParam.put("dateini", evalScriptLaunchDto.getDateIni());
                requestParam.put("datefin", evalScriptLaunchDto.getDateFin());
                requestParam.put("polygon",  evalScriptLaunchDto.getPolygon());
                requestParam.put("collection",  evalScriptLaunchDto.getCollection());
                requestParam.put("clienteid",  copernicuscredentials.getClientid());
                requestParam.put("secret",  copernicuscredentials.getSecret());
                requestParam.put("token",  copernicuscredentials.getToken());
                requestParam.put("maxcloudcoverage",  evalScriptLaunchDto.getMaxcloudcoverage());
                requestParam.put("script",  dtoEvalscript.getScriptText());
                requestParam.put("resolution", evalScriptLaunchDto.getResolution());

                //Comprobamos el patron
                //Se invoca a la URL
                Request request1 = new Request(urltext, requestParam);
                String jsonArray = request1.output;
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray);
                    Iterator<String> keys = jsonObject.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (jsonObject.get(key) instanceof JSONObject) {
                            // do something with jsonObject here
                            JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                            Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                            while (keysint.hasNext()) {
                                String keyint = keysint.next();
                                System.out.println("valores para el json");
                                System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                        jsonObject1.get(keyint).toString());
                                ListfilesEvalScript itemlistfilesevalscriptCheck = findfileevalscriptrecord(keyint);
                                System.out.println("qqqqqqqq");
                                if (itemlistfilesevalscriptCheck.getKey().equals(-1)) {
                                    System.out.println("valores para el json no existe indice");
                                    ListfilesEvalScript itmlistfilesnew = new ListfilesEvalScript();
                                    Setlistfilesevalscriptfield(key, jsonObject1, keyint, itemlistfilesevalscriptCheck, 0);
                                } else {
                                    // You use this ".get()" method to actually get your Listfiles from the Optional object
                                    System.out.println("/api/listfiles/executeevalscript post   valores para el json existe indice");

                                    objlistfilesevalscript.remove(itemlistfilesevalscriptCheck);
                                    Setlistfilesevalscriptfield(key, jsonObject1, keyint, itemlistfilesevalscriptCheck, 1);
                                }
                            }
                        }
                    }
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }



                if (!objlistfilesevalscript.isEmpty()){
                    //Actualizamos el path
                    evalScriptLaunchDto.setPathtiff(objlistfilesevalscript.get(0).getPath());
                    evalScriptLaunchDto.setPathjson(objlistfilesevalscript.get(1).getPath());
                    //guardamos los datos
                    EvalScriptLaunchDto evalScriptLaunchDto1 = evalScriptLaunchService.guardar(evalScriptLaunchDto);
                    interfazConPantalla.addAttribute("consulta",evalScriptLaunchDto);
                    //return String.format("redirect:/sentinelqueryfilestiff/filter/"+ sentinelQueryFiles.get().getId());
                    return "evalscript/launchresultados";
                }
                else {

                    return "sentinelqueryfiles/detallesnoencontrado";
                }

            } catch (Exception e) {
                System.out.println("Error Message");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
                return String.format("redirect:/sentinelqueryfiles");
            }
        } else {
            interfazConPantalla.addAttribute("consulta",copernicuscredentials);
            return "redirect:/api/credentials";
        }
    }


    @GetMapping("/api/csvdata")
    public String csvdataGet(ModelMap interfazConPantalla) throws IOException {
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/csvdatosprocesados/";
        Request  request1 = new Request(urltext);
        EscribeCSVDto  escribeCSVDto = new EscribeCSVDto();
        try {

            /*System.out.println("request1.getResponseHeaders()");
            System.out.println(request1.getResponseHeaders());
            System.out.println("request1.output");
            System.out.println(request1.output);*/
            escribeCSVDto.setResultado(request1.output);
            interfazConPantalla.addAttribute("file",escribeCSVDto);
        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println();
            escribeCSVDto.setResultado("Error Message :" + e.getClass().getSimpleName() +  e.getMessage());
            interfazConPantalla.addAttribute("file",escribeCSVDto );
        }
        return "api/obtenercsv";
    }
    @PostMapping("/api/csvdata")
    public String csvdataPost(@ModelAttribute(name ="file") EscribeCSVDto escribeCSVDto,Model interfazConPantalla) throws Exception {
        try {
            if (escribeCSVDto.getNombrearchivo().length() > 5 && escribeCSVDto.getRuta().length() > 4 ){
                String filename = escribeCSVDto.getRuta() + "/" + escribeCSVDto.getNombrearchivo();
                File csvFile = new File(filename);
                FileWriter fileWriter = new FileWriter(csvFile);
                System.out.println("long cadena");
                System.out.println(escribeCSVDto.getResultado().length());
                fileWriter.write(escribeCSVDto.getResultado());
                fileWriter.close();
                interfazConPantalla.addAttribute("resultado","El archivo se ha guardado correctamente ");
            }
            else
            {
                interfazConPantalla.addAttribute("resultado","El nombre del archivo o la ruta no son correctos ");
            }

        } catch (Exception e) {
            interfazConPantalla.addAttribute("resultado","Error Message :" + e.getClass().getSimpleName() +  e.getMessage() );
        }
        return "api/obtenercsvresultado";
    }

    /**
     *
     * @param key
     * @return
     */
    private DatosLucas2018Api finddatoslucas2018OCrecord(String key){
        DatosLucas2018Api listini = new DatosLucas2018Api();
        listini.setKeyapi(-1);
        for (DatosLucas2018Api p : objlistdatoslucas2018Api) {
            if (p.getKeyapi().equals(Integer.parseInt(key))) {
                listini = p;
            }
        }
        return listini;
    }
    private void Setlistdatoslucas2018OCfield(String key, JSONObject jsonObject1, String keyint,
                                              DatosLucas2018Api listItm  , Integer option) {
        switch (key.toLowerCase()) {
            case "longitud":
                listItm.setLongitud(jsonObject1.get(keyint).toString());
                break;
            case "latitud":
                listItm.setLatitud(jsonObject1.get(keyint).toString());
                break;
            case "id":
                listItm.setId(jsonObject1.get(keyint).toString());
                break;
            case "path":
                listItm.setPath(jsonObject1.get(keyint).toString());
                break;
            case "reflectance":
                listItm.setReflectance(jsonObject1.get(keyint).toString());
                break;
            case "band":
                listItm.setBand(jsonObject1.get(keyint).toString());
                break;
            case "survey_date":
                listItm.setSurvey_date(jsonObject1.get(keyint).toString());
                break;
            case "oc":
                listItm.setOc(jsonObject1.get(keyint).toString());
                break;
            case "key":
                listItm.setKeyapi(Integer.valueOf(keyint));
                break;
        }
        if (option > 0){
            //el elemennto existe y se sustituye
            objlistdatoslucas2018Api.add(listItm);

        }else {
            //el elemennto no existe y se añadeç
            listItm.setKeyapi( Integer.parseInt(keyint));
            objlistdatoslucas2018Api.add(listItm);
        }

    }
    public Page<DatosLucas2018Api> getLucas2018OCpages(int page, int size) {

        Pageable pageRequest = createPageRequestUsing(page, size);

        List<DatosLucas2018Api> all = objlistdatoslucas2018Api;
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), all.size());

        List<DatosLucas2018Api> pageContent = all.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, all.size());
    }

    @GetMapping ("/api/lucas/lucas2018Search")
    public String lucas2018list (@ModelAttribute(name ="datos") DatosLucasSearchDto datosLucasSEarchDto,
                                 Model interfazConPantalla){
        DatosLucasSearchDto datosLucasSearchDto = new DatosLucasSearchDto();
        interfazConPantalla.addAttribute("datos",datosLucasSearchDto);
        return "lucas/search";
    }
    @GetMapping ("/api/lucas/lucas2018Export")
    public ResponseEntity<byte[]> lucas2018listCsvFile (@ModelAttribute(name ="datos") DatosLucasSearchDto datosLucasSEarchDto,
                                                        Model interfazConPantalla){
        System.out.println("lucas2018listCsvFile: paso1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "DatosLucas2018Api.csv");
        System.out.println("lucas2018listCsvFile: paso3");
        byte[] csvBytes = csvGeneratorUtil.generateCsv(objlistdatoslucas2018Api).getBytes();
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
    @GetMapping ("/api/uploadedfiles/csvexport")
    public ResponseEntity<byte[]> csvuserFile (@ModelAttribute(name ="datos") DatosLucasSearchDto datosLucasSEarchDto,
                                                        Model interfazConPantalla){
        System.out.println("csvuserFile: paso1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "csvreflectance.csv");
        System.out.println("csvuserFile: paso3");
        byte[] csvBytes = csvGeneratorUtil.generateCsvdatosusr(objListUploadedFilesReflectances).getBytes();
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
    @PostMapping ("/api/lucas/lucas2018list")
    public String lucas2018list (@ModelAttribute(name ="datos") DatosLucasSearchDto datosLucasSEarchDto,
                                 Model interfazConPantalla,HttpSession session) throws Exception {
        //componemos la url
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/obtenerlucas2018proc/";
        //Obtenemos los datos del usuario de la sesión
        String userName = "no informado";
        SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(superCustomerUserDetails.getUsername());


        try {
            JSONObject requestParam = new JSONObject();
            requestParam.put("userid", superCustomerUserDetails.getUserID());
            requestParam.put("dateIni", datosLucasSEarchDto.getDateIni());
            requestParam.put("dateFin", datosLucasSEarchDto.getDateFin());
            requestParam.put("polygon", datosLucasSEarchDto.getPolygon());
            requestParam.put("ref", "lucas2018OCND");
            //Comprobamos el patron
            //Se invoca a la URL
            System.out.println("Post pantalla de busqueda 50");
            Request request1 = new Request(urltext, requestParam);
            String jsonArray = request1.output;
            try {
                JSONObject jsonObject = new JSONObject(jsonArray);
                Iterator<String> keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (jsonObject.get(key) instanceof JSONObject) {
                        // do something with jsonObject here
                        JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                        Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                        while (keysint.hasNext()) {
                            String keyint = keysint.next();
                            System.out.println("valores para el json");
                            System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                    jsonObject1.get(keyint).toString());
                            DatosLucas2018Api itemlistCheck = finddatoslucas2018OCrecord(keyint);
                            System.out.println("qqqqqqqq");
                            if (itemlistCheck.getKeyapi().equals(-1)) {
                                System.out.println("valores para el json no existe indice");

                                Setlistdatoslucas2018OCfield(key, jsonObject1, keyint, itemlistCheck, 0);
                            } else {
                                // You use this ".get()" method to actually get your Listfiles from the Optional object
                                System.out.println("/api/listfiles/executeevalscript post   valores para el json existe indice");

                                objlistdatoslucas2018Api.remove(itemlistCheck);
                                Setlistdatoslucas2018OCfield(key, jsonObject1, keyint, itemlistCheck, 1);
                            }
                        }
                    }
                }
            } catch (JSONException err) {
                System.out.println("Error: " + err.toString());
            }
            //id unico de la busqueda
            Random r = new Random();
            int low = 10;
            int high = 1000000;
            int result = r.nextInt(high-low) + low;
            //comprobamos si hay datos
            if (!objlistdatoslucas2018Api.isEmpty()){
                //Guardamos los datos en la tabla
                Iterator<DatosLucas2018Api> it = objlistdatoslucas2018Api.iterator();
                List<DatosLucas2018Dto> lstdatosLucas2018Dto = new ArrayList<>();
                // mientras al iterador queda proximo juego
                while(it.hasNext()){
                    //Obtenemos la password de a entidad
                    //Datos del usuario
                    System.out.println("Encuentro elemento");
                    DatosLucas2018Dto dto = new DatosLucas2018Dto();
                    DatosLucas2018Api dtoapi =  it.next();
                    System.out.println("Iterador leigo id:");
                    System.out.println(dtoapi.getId());
                    dto.setSearchid(result);
                    dto.setBand(dtoapi.getBand());
                    dto.setPointid(dtoapi.getId());
                    dto.setPath(dtoapi.getPath());
                    dto.setReflectance(dtoapi.getReflectance());
                    dto.setLatitud(dtoapi.getLatitud());
                    dto.setLongitud(dtoapi.getLongitud());
                    dto.setKeyapi(dtoapi.getKeyapi());
                    dto.setOc(dtoapi.getOc());
                    dto.setSurvey_date(dtoapi.getSurvey_date());
                    System.out.println("Iterador leigo y antes de guardado id:");
                    System.out.println(dto.getPointid());

                    DatosLucas2018Dto dtosaved = datosLucas2018Service.guardar(dto);
                    System.out.println("Encuentro elemento guardado");
                    System.out.println(dtosaved.getId());
                    System.out.println(dtosaved.getPointid());
                    lstdatosLucas2018Dto.add(dtosaved);
                }
                //Ordenamos la lista
                List<DatosLucas2018Dto> sortedItems = lstdatosLucas2018Dto.stream()
                        .sorted(Comparator.comparing(DatosLucas2018Dto::getBand))
                        .sorted(Comparator.comparing(DatosLucas2018Dto::getPointid))
                        .collect(Collectors.toList());
                datosLucas2018Service.guardar(sortedItems);

                List<DatosLucas2018Dto> datosLucas2018Dtos = datosLucas2018Service.getlucasfloat(result,"%FLOAT32%");
                interfazConPantalla.addAttribute("lista", datosLucas2018Dtos);
                return "lucas/lista";
            }
            else {
                return "lucas/detallesnoencontrado";
            }

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
            return "lucas/detallesnoencontrado";
        }
    }

    @GetMapping ("/api/uploadedfiles/replectance/{id}")
    public String csvreflectancelist (@PathVariable("id") Integer id,Model interfazConPantalla,HttpSession session) throws Exception {
        Optional<UploadedFilesDto> optuploadedFilesDto = uploadedFilesService.encuentraPorId(id);
        System.out.println("getreflecance : elemento leido");
        if (optuploadedFilesDto.isPresent()) {
            //componemos la url
            String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/obtenerreflectancecsv/";
            //Obtenemos los datos del usuario de la sesión
            String userName = "no informado";
            SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(superCustomerUserDetails.getUsername());


            try {
                JSONObject requestParam = new JSONObject();
                requestParam.put("userid", superCustomerUserDetails.getUserID());
                requestParam.put("csvid", optuploadedFilesDto.get().getId());

                //Comprobamos el patron
                //Se invoca a la URL
                System.out.println("Post pantalla de busqueda 50");
                Request request1 = new Request(urltext, requestParam);
                String jsonArray = request1.output;
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray);
                    Iterator<String> keys = jsonObject.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (jsonObject.get(key) instanceof JSONObject) {
                            // do something with jsonObject here
                            JSONObject jsonObject1 = ((JSONObject) jsonObject.getJSONObject(key));
                            Iterator<String> keysint = ((JSONObject) jsonObject.get(key)).keys();
                            while (keysint.hasNext()) {
                                String keyint = keysint.next();
                                System.out.println("valores para el json");
                                System.out.println("Key:" + key + ":: keyint:" + keyint + "::  value:" +
                                        jsonObject1.get(keyint).toString());
                                UploadedFilesReflectance itemlistCheck = findUploadedFilesReflectancerecord(keyint);

                                if (itemlistCheck.getKey().equals(-1)) {
                                    System.out.println("valores para el json no existe indice");

                                    SetUploadedFilesReflectancefield(key, jsonObject1, keyint, itemlistCheck, 0);
                                } else {
                                    // You use this ".get()" method to actually get your Listfiles from the Optional object
                                    System.out.println("/api/listfiles/executeevalscript post   valores para el json existe indice");

                                    objListUploadedFilesReflectances.remove(itemlistCheck);
                                    SetUploadedFilesReflectancefield(key, jsonObject1, keyint, itemlistCheck, 1);
                                }
                            }
                        }
                    }
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }
                //comprobamos si hay datos
                if (!objListUploadedFilesReflectances.isEmpty()) {
                    //Se guardan los datos en la tabla

                    //Se descargan las imagenes

                    //Se muestra el listado
                    interfazConPantalla.addAttribute("lista", objListUploadedFilesReflectances);
                    return "upload/reflectance";
                } else {
                    return "upload/detallesnoencontrado";
                }

            } catch (Exception e) {
                System.out.println("Error Message");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
                return "upload/detallesnoencontrado";
            }
        }
        else {
            return "lucas/detallesnoencontrado";
        }
    }
    private UploadedFilesReflectance findUploadedFilesReflectancerecord(String key){
        UploadedFilesReflectance listini = new UploadedFilesReflectance();
        listini.setKey(-1);
        for (UploadedFilesReflectance p : objListUploadedFilesReflectances) {
            if (p.getKey().equals(Integer.parseInt(key))) {
                listini = p;
            }
        }
        return listini;
    }
    private void SetUploadedFilesReflectancefield(String key, JSONObject jsonObject1, String keyint,
                                                     UploadedFilesReflectance listItm  , Integer option) {
        switch (key.toLowerCase()) {
            case "longitud":
                listItm.setLongitude(jsonObject1.get(keyint).toString());
                break;
            case "latitud":
                listItm.setLatitude(jsonObject1.get(keyint).toString());
                break;
            case "id":
                listItm.setId(jsonObject1.get(keyint).toString());
                break;
            case "path":
                listItm.setPath(jsonObject1.get(keyint).toString());
                break;
            case "reflectance":
                listItm.setReflectance(jsonObject1.get(keyint).toString());
                break;
            case "band":
                listItm.setBand(jsonObject1.get(keyint).toString());
                break;
            case "userid":
                listItm.setUserid(jsonObject1.get(keyint).toString());
                break;
            case "soc":
                listItm.setSoc(jsonObject1.get(keyint).toString());
                break;
        }
        if (option > 0){
            //el elemennto existe y se sustituye
            objListUploadedFilesReflectances.add(listItm);

        }else {
            //el elemennto no existe y se añadeç
            listItm.setKey( Integer.parseInt(keyint));
            objListUploadedFilesReflectances.add(listItm);
        }

    }



    //End




    @GetMapping("/api/test")
    public String testapicall(ModelMap interfazConPantalla) throws IOException {
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/listar?reference=02_UBU";
        URL url = new URL(urltext);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();


        //Getting the response code
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            interfazConPantalla.addAttribute("resultado","Error en la llamada al api response code:" + responsecode );
        } else {

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();
            interfazConPantalla.addAttribute("resultado",inline);
        }

        return "api/mostrarresultadoapi";
    }
    @GetMapping("/api/testlist")
    public String testapilist(ModelMap interfazConPantalla) throws IOException {
        String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/listar?reference=02_UBU";
        URL url = new URL(urltext);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();


        //Getting the response code
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            interfazConPantalla.addAttribute("resultado","Error en la llamada al api");
        } else {

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();
            interfazConPantalla.addAttribute("resultado",inline);
        }
        return "usuarios/mostrarresultadoapi";
    }
}
