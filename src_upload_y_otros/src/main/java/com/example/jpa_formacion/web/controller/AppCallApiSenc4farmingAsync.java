package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.apiitem.DatosLucas2018Api;
import com.example.jpa_formacion.apiitem.Listfiles;
import com.example.jpa_formacion.apiitem.ListfilesEvalScript;
import com.example.jpa_formacion.apiitem.Listfilestiff;
import com.example.jpa_formacion.config.ConfiguationProperties;
import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.*;

import com.example.jpa_formacion.model.SentinelQueryFiles;
import com.example.jpa_formacion.service.*;
import com.example.jpa_formacion.util.CsvGeneratorUtil;

import jakarta.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;


import java.io.*;
import java.util.*;


@Controller
public class AppCallApiSenc4farmingAsync extends AbstractController <GrupoTrabajoDto> {
    @Autowired
    private CsvGeneratorUtil csvGeneratorUtil;

    private final GrupoService service;
    private final FiltroListarArchivosService filtroListarArchivosService;
    private final SentinelQueryFilesService sentinelQueryFilesService;

    private final SentinelQueryFilesTiffService sentinelQueryFilesTiffService;
    private final EvalScriptService evalScriptService;
    private final EvalScriptLaunchService evalScriptLaunchService;
    private List<Listfiles> objlistfiles;

    private List<Listfilestiff> objlistfilestiff;

    private List<ListfilesEvalScript> objlistfilesevalscript;

    private List<DatosLucas2018Api> objlistdatoslucas2018Api;
    private final UsuarioService  usuarioService;

    private final UploadedFilesService uploadedFilesService;

    private final ConfiguationProperties configuationProperties;
    private static final HttpClient AsymcHttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public AppCallApiSenc4farmingAsync(MenuService menuService, GrupoService service,
                                       FiltroListarArchivosService filtroListarArchivosService,
                                       SentinelQueryFilesService sentinelQueryFilesService,
                                       SentinelQueryFilesTiffService sentinelQueryFilesTiffService,
                                       EvalScriptService evalScriptService, EvalScriptLaunchService evalScriptLaunchService,
                                       List<Listfilestiff> objlistfilestiff,
                                       UsuarioService usuarioService,
                                       UploadedFilesService uploadedFilesService, ConfiguationProperties configuationProperties) {
        super(menuService);
        this.service = service;
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
        this.objlistdatoslucas2018Api = new ArrayList<>();
        this.usuarioService = usuarioService;
    }
    /**
     * Example for sending an asynchronous POST request
     *
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    private static void demo3() throws ExecutionException, InterruptedException {

        System.out.println("Demo 3");

        var postRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://postman-echo.com/post"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString("Hi there!"))
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        var client = HttpClient.newBuilder().executor(executor).build();

        var responseFuture = client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenApply(res -> {
                    System.out.printf("StatusCode: %s%n", res.statusCode());
                    return res;
                })
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .get();

        executor.shutdownNow();
    }
    @GetMapping ("/api/uploadedfiles/getreflecance/{id}")
    public String getreplectancecsv (@PathVariable("id") Integer id,Model interfazConPantalla,HttpSession session) throws Exception {
        //componemos la url
        Optional<UploadedFilesDto> optuploadedFilesDto = uploadedFilesService.encuentraPorId(id);
        System.out.println("getreflecance : elemento leido");
        if (optuploadedFilesDto.isPresent()){
            System.out.println("getreflecance : elemento encontrado");
            String urltext = "http://" + configuationProperties.getIppythonserver() + ":8100/api/proc/getreflecancecsvcoords/";
            //Obtenemos los datos del usuario de la sesión
            String userName = "no informado";
            SuperCustomerUserDetails superCustomerUserDetails = (SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(superCustomerUserDetails.getUsername());
            //Convert csv file to json
            String csvFile = optuploadedFilesDto.get().getPath() + "/" + optuploadedFilesDto.get().getDescription();
            //Como encontré datos, leo el csv
            BufferedReader br = new BufferedReader(new FileReader(optuploadedFilesDto.get().getPath()+"/"+ optuploadedFilesDto.get().getDescription()));
            List<UploadedFilesContentDto> csvitemslist = new ArrayList<>();
            String line = br.readLine();
            String splitBy = ",";
            Integer numline = 0;
            System.out.println("getreflecance : paso 1");
            try {
                JSONObject requestParam = new JSONObject();
                requestParam.put("userid", superCustomerUserDetails.getUserID());
                requestParam.put("csvid", optuploadedFilesDto.get().getId());
                System.out.println("getreflecance : paso 2");
                while ((line = br.readLine()) != null) {
                    // split on comma(',')
                    numline = numline + 1;

                    String[] datosCsv = line.split(splitBy);

                    // create car object to store values
                    UploadedFilesContentDto dto = new UploadedFilesContentDto();
                    JSONObject csvline = new JSONObject();
                    // add values from csv to car object
                    csvline.put("longitude",datosCsv[0]);
                    csvline.put("latitude",datosCsv[1]);
                    csvline.put("soc",datosCsv[2]);
                    csvline.put("date",datosCsv[6]);
                    // adding item to json
                    requestParam.put("linea"+ String.valueOf(numline),csvline);

                }
                System.out.println("getreflecance : paso 3");
                requestParam.put("numlines",numline);
                System.out.println(requestParam.toString());
                // Comprobamos el patron
                //Se invoca a la URL
                System.out.println("Post pantalla de busqueda 50");
                //Lanzamos la peticioon asincrona
                byte[] request = requestParam.toString().getBytes();

                var postRequest = HttpRequest.newBuilder()
                        .uri(URI.create(urltext))
                        .version(HttpClient.Version.HTTP_2)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofByteArray(request))
                        .build();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                var client = HttpClient.newBuilder().executor(executor).build();

                var responseFuture = client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());
                interfazConPantalla.addAttribute("resultado", "Process launched, view csv reflectance in 15 minutes");
                return "upload/processlaunched";
            } catch (Exception e) {
                System.out.println("Error Message");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
                interfazConPantalla.addAttribute("resultado", "Error Message :" + e.getClass().getSimpleName() + e.getMessage());
                return "upload/processlaunched";
            }
        } else {
            interfazConPantalla.addAttribute("resultado", "csv file not found");
            return "upload/processlaunched";
        }
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/api/listfiles/downloadbands/async/{idquery}")
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
                //Se invoca a la URL asincronamente
                System.out.println("Post pantalla de busqueda 50");
                //Lanzamos la peticioon asincrona
                byte[] request = requestParam.toString().getBytes();

                var postRequest = HttpRequest.newBuilder()
                        .uri(URI.create(urltext))
                        .version(HttpClient.Version.HTTP_2)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofByteArray(request))
                        .build();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                var client = HttpClient.newBuilder().executor(executor).build();

                var responseFuture = client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

                String jsonArray = responseFuture.thenApply(res -> {
                            System.out.printf("StatusCode: %s%n", res.statusCode());
                            return res;
                        })
                        .thenApply(HttpResponse::body)
                        .thenAccept(System.out::println)
                        .get().toString();

                executor.shutdownNow();
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
    @PostMapping("/api/listfiles/downloadbands/async/wait/{idquery}")
    public String listfilesDownloadbandswait(@PathVariable("idquery") Integer id, Model interfazConPantalla,HttpSession session) throws Exception {
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
                //Se invoca a la URL asincronamente
                System.out.println("Post pantalla de busqueda 50");
                //Lanzamos la peticioon asincrona
                byte[] request = requestParam.toString().getBytes();

                HttpRequest postRequest = HttpRequest.newBuilder()
                        .uri(URI.create(urltext))
                        .version(HttpClient.Version.HTTP_2)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofByteArray(request))
                        .build();
                CompletableFuture<HttpResponse<String>> AsyncResponse = null;

                // sendAsync(): Sends the given request asynchronously using this client with the given response body handler.
                //Equivalent to: sendAsync(request, responseBodyHandler, null).
                AsyncResponse = AsymcHttpClient.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

                String strAsyncResultBody = null;
                int intAsyncResultStatusCode = 0;

                try {
                    strAsyncResultBody = AsyncResponse.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

                    // OR:

                    // join(): Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally.
                    // To better conform with the use of common functional forms,
                    // if a computation involved in the completion of this CompletableFuture threw an exception,
                    // this method throws an (unchecked) CompletionException with the underlying exception as its cause.

                    HttpResponse<String> response = AsyncResponse.join();
                    intAsyncResultStatusCode = AsyncResponse.thenApply(HttpResponse::statusCode).get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                System.out.println("=============== AsyncHTTPClient Body:===============  \n" + strAsyncResultBody);
                System.out.println("\n=============== AsyncHTTPClient Status Code:===============  \n" + intAsyncResultStatusCode);


                String jsonArray = strAsyncResultBody;
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
}
