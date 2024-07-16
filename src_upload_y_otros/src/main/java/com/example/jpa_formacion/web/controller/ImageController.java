package com.example.jpa_formacion.web.controller;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.logging.SocketHandler;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.DatosLucas2018Dto;
import com.example.jpa_formacion.dto.EvalScriptLaunchDto;
import com.example.jpa_formacion.dto.SentinelQueryFilesTiffDto;
import com.example.jpa_formacion.model.DatosLucas2018;
import com.example.jpa_formacion.service.*;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.*;


@Controller
public class ImageController extends AbstractController <SentinelQueryFilesTiffDto>{

    @Autowired
    FilesStorageService storageService;
    private final SentinelQueryFilesTiffService service;

    private final DatosLucas2018Service datosLucas2018Service;

    private final EvalScriptLaunchService evalScriptLaunchService;

    public ImageController(MenuService menuService, SentinelQueryFilesTiffService service, DatosLucas2018Service datosLucas2018Service, EvalScriptLaunchService evalScriptLaunchService) {
        super(menuService);
        this.service = service;
        this.datosLucas2018Service = datosLucas2018Service;
        this.evalScriptLaunchService = evalScriptLaunchService;
    }

    @GetMapping("/visor/images/new")
    public String newImage(Model model) {
        return "upload_form";
    }

    @PostMapping("/visor/images/upload")
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            storageService.save(file);

            message = "Uploaded the image successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "/visor/upload_form";
    }

    @GetMapping("/visor/image/evalscript/{id}")
    public String getListImagesEvalscript(@PathVariable("id") Integer id,Model model) {
        Optional<EvalScriptLaunchDto> evalScriptLaunchDto = evalScriptLaunchService.encuentraPorId(id);
        String pathcompleto =  evalScriptLaunchDto.get().getPathtiff();

        String path = pathcompleto.substring(0,pathcompleto.lastIndexOf("/"));
        String filename = pathcompleto.substring(pathcompleto.lastIndexOf("/")+1,pathcompleto.length());
        String nombredirinterno =  pathcompleto.substring(pathcompleto.lastIndexOf("userfiles/")+10,(pathcompleto.lastIndexOf("/")));
        //String uploadDir = "src_user_data/sentinel/" + nombredirinterno ;
        String uploadDir = "src/files/src_data_safe/" + nombredirinterno ;
        String internalpath = uploadDir + "/response.tiff";
        System.out.println("Path:");
        System.out.println(internalpath);
        model.addAttribute("image", internalpath);

        return "/visor/images";
    }

    @GetMapping("/visor/image/{id}")
    public String getListImages(@PathVariable("id") Integer id,Model model) throws IOException, ParseException {
        Optional<SentinelQueryFilesTiffDto> sentinelQueryFilesTiffDto = service.encuentraPorId(id);
        String pathcompleto =  sentinelQueryFilesTiffDto.get().getPath();
        SentinelQueryFilesTiffDto sentinelQueryFilesTiffDto1 = sentinelQueryFilesTiffDto.get();
        String polygon = sentinelQueryFilesTiffDto1.getSentinelQueryFilesfortiff().getFiltroListarArchivos().getPolygon();
        String nombredirinterno =  pathcompleto.substring(pathcompleto.lastIndexOf("userfiles/"),(pathcompleto.lastIndexOf("/")));
        String uploadDir = "files/src_data_safe/" + nombredirinterno ;
        String internalpath = uploadDir + "/response.tiff";
        String internalpathjson =  "api/"+uploadDir + "/request.json";
        System.out.println("Path:");
        System.out.println(internalpath);
        System.out.println(polygon);
        //Falta leer el json
        Object o = new JSONParser().parse(new FileReader(internalpathjson));
        JSONObject jsonObject = (JSONObject) o;
        Number minlong = 0;
        Number minlat = 0;
        Number maxlong = 0;
        Number maxlat = 0;
        if ( jsonObject.get("request") instanceof JSONObject ) {
            System.out.println("lvl0");
            Object jsonrequest = jsonObject.get("request");
            JSONObject lvl1 = new JSONObject((Map) jsonrequest);
            if ( lvl1.get("payload") instanceof JSONObject ) {
                System.out.println("lvl1");
                Object jsonpayload = lvl1.get("payload");
                JSONObject lvl2 = new JSONObject((Map) jsonpayload);
                if (  lvl2.get("input") instanceof JSONObject ) {
                    System.out.println("lvl2");
                    Object jsoninput = lvl2.get("input");
                    JSONObject lvl3 = new JSONObject((Map) jsoninput);
                    if (lvl3.get("bounds") instanceof JSONObject) {
                        System.out.println("lvl3");
                        Object jsonbounds = lvl3.get("bounds");
                        JSONObject lvl4 = new JSONObject((Map) jsonbounds);
                        if (lvl3.get("bounds") instanceof JSONObject) {
                            System.out.println("lvl4");
                            Object cc =  lvl4.get("bbox");
                            if (cc instanceof JSONArray) {
                                JSONArray last = (JSONArray) cc;
                                Iterator i = last.iterator();
                                Integer iter = 0;
                                while(i.hasNext()) {
                                    Number val = (Number) i.next();
                                    if ( iter.equals(0))
                                        minlong = val;
                                    if ( iter.equals(1))
                                        minlat = val;
                                    if ( iter.equals(2))
                                        maxlong = val;
                                    if ( iter.equals(3))
                                        maxlat = val;
                                    iter +=1;
                                }
                            }

                        }
                    }
                }
            }
        }

        System.out.println("coords minlong : " + minlong );
        System.out.println("coords minlat : " + minlat );
        System.out.println("coords  maxlong: " + maxlong );
        System.out.println("coords  maxlat : " +  maxlat );

        //Buscar bounds
        //Enviar min lond, min lat , maz long max lat
        // usar dichos valores para mostrar el poígono
        model.addAttribute("minlong", minlong);
        model.addAttribute("minlat", minlat);
        model.addAttribute("maxlong", maxlong);
        model.addAttribute("maxlat", maxlat);
        model.addAttribute("image", internalpath);
        model.addAttribute("polygon", polygon);

        return "/visor/images";
    }
    @GetMapping("/visor/image/{id}/{idprev}")
    public String getListImagesBack(@PathVariable("id") Integer id,
                                    @PathVariable("idprev") Integer idprev,
                                    Model model) throws IOException, ParseException {
        Optional<SentinelQueryFilesTiffDto> sentinelQueryFilesTiffDto = service.encuentraPorId(id);
        String pathcompleto =  sentinelQueryFilesTiffDto.get().getPath();
        SentinelQueryFilesTiffDto sentinelQueryFilesTiffDto1 = sentinelQueryFilesTiffDto.get();
        String polygon = sentinelQueryFilesTiffDto1.getSentinelQueryFilesfortiff().getFiltroListarArchivos().getPolygon();
        String nombredirinterno =  pathcompleto.substring(pathcompleto.lastIndexOf("userfiles/"),(pathcompleto.lastIndexOf("/")));
        String uploadDir = "files/src_data_safe/" + nombredirinterno ;
        String internalpath = uploadDir + "/response.tiff";
        String internalpathjson =  "api/"+uploadDir + "/request.json";
        System.out.println("Path:");
        System.out.println(internalpath);
        System.out.println(polygon);
        //Falta leer el json
        Object o = new JSONParser().parse(new FileReader(internalpathjson));
        JSONObject jsonObject = (JSONObject) o;
        Number minlong = 0;
        Number minlat = 0;
        Number maxlong = 0;
        Number maxlat = 0;
        if ( jsonObject.get("request") instanceof JSONObject ) {
            System.out.println("lvl0");
            Object jsonrequest = jsonObject.get("request");
            JSONObject lvl1 = new JSONObject((Map) jsonrequest);
            if ( lvl1.get("payload") instanceof JSONObject ) {
                System.out.println("lvl1");
                Object jsonpayload = lvl1.get("payload");
                JSONObject lvl2 = new JSONObject((Map) jsonpayload);
                if (  lvl2.get("input") instanceof JSONObject ) {
                    System.out.println("lvl2");
                    Object jsoninput = lvl2.get("input");
                    JSONObject lvl3 = new JSONObject((Map) jsoninput);
                    if (lvl3.get("bounds") instanceof JSONObject) {
                        System.out.println("lvl3");
                        Object jsonbounds = lvl3.get("bounds");
                        JSONObject lvl4 = new JSONObject((Map) jsonbounds);
                        if (lvl3.get("bounds") instanceof JSONObject) {
                            System.out.println("lvl4");
                            Object cc =  lvl4.get("bbox");
                            if (cc instanceof JSONArray) {
                                JSONArray last = (JSONArray) cc;
                                Iterator i = last.iterator();
                                Integer iter = 0;
                                while(i.hasNext()) {
                                    Number val = (Number) i.next();
                                    if ( iter.equals(0))
                                        minlong = val;
                                    if ( iter.equals(1))
                                        minlat = val;
                                    if ( iter.equals(2))
                                        maxlong = val;
                                    if ( iter.equals(3))
                                        maxlat = val;
                                    iter +=1;
                                }
                            }

                        }
                    }
                }
            }
        }

        System.out.println("coords minlong : " + minlong );
        System.out.println("coords minlat : " + minlat );
        System.out.println("coords  maxlong: " + maxlong );
        System.out.println("coords  maxlat : " +  maxlat );

        //Buscar bounds
        //Enviar min lond, min lat , maz long max lat
        // usar dichos valores para mostrar el poígono
        model.addAttribute("minlong", minlong);
        model.addAttribute("minlat", minlat);
        model.addAttribute("maxlong", maxlong);
        model.addAttribute("maxlat", maxlat);
        model.addAttribute("image", internalpath);
        model.addAttribute("polygon", polygon);
        model.addAttribute("idprev", idprev);

        return "/visor/imagesprev";
    }
    @GetMapping("/visor/image/lucas/{id}")
    public String getPathImags(@PathVariable("id") Long id,Model model) throws IOException, ParseException {
        Optional<DatosLucas2018Dto> datosLucas2018Dto = datosLucas2018Service.encuentraPorId(id);
        String pathcompleto =  datosLucas2018Dto.get().getPath();
        //obtenemos el id del usuario conectado
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        //Traducimos a  la imagen en local
        System.out.println("Path inicial:" + pathcompleto);


        String internaltifffloat32path = pathcompleto.replace("/app/","api/" );
        String internaljsonfloat32path = internaltifffloat32path.replace("response.tiff","request.json" );
        //Buscanmos el path para la imagen a mostrar
        //Obtenemos el id del punto y la banda
        Optional<DatosLucas2018> datosLucas2018 = datosLucas2018Service.buscar(id);
        // Con los datos buscamos los registros con auto el json y el tiff
        //Buscamos el tiff
        DatosLucas2018 datosLucas20181TiffAuto = datosLucas2018Service.getlucasreglike(
                datosLucas2018.get().getSearchid(),
                datosLucas2018.get().getPointid(),
                datosLucas2018.get().getBand(), "%AUTO%response.tiff");
        //
        String internaltiffautopath = datosLucas20181TiffAuto.getPath().replace("/app/","");;
        //¿Existe?
        File f = new File(internaltiffautopath);
        if (f.exists()){
            System.out.println("internaltiffautopath  encontrado");
        }
        String internaljsonautopath =  internaltiffautopath.replace("response.tiff","request.json" );

        //Falta leer el json
        Object o = new JSONParser().parse(new FileReader(internaljsonfloat32path));
        JSONObject jsonObject = (JSONObject) o;
        Number minlong = 0;
        Number minlat = 0;
        Number maxlong = 0;
        Number maxlat = 0;
        if ( jsonObject.get("request") instanceof JSONObject ) {
            System.out.println("lvl0");
            Object jsonrequest = jsonObject.get("request");
            JSONObject lvl1 = new JSONObject((Map) jsonrequest);
            if ( lvl1.get("payload") instanceof JSONObject ) {
                System.out.println("lvl1");
                Object jsonpayload = lvl1.get("payload");
                JSONObject lvl2 = new JSONObject((Map) jsonpayload);
                if (  lvl2.get("input") instanceof JSONObject ) {
                    System.out.println("lvl2");
                    Object jsoninput = lvl2.get("input");
                    JSONObject lvl3 = new JSONObject((Map) jsoninput);
                    if (lvl3.get("bounds") instanceof JSONObject) {
                        System.out.println("lvl3");
                        Object jsonbounds = lvl3.get("bounds");
                        JSONObject lvl4 = new JSONObject((Map) jsonbounds);
                        if (lvl3.get("bounds") instanceof JSONObject) {
                            System.out.println("lvl4");
                            Object cc =  lvl4.get("bbox");
                            if (cc instanceof JSONArray) {
                                JSONArray last = (JSONArray) cc;
                                Iterator i = last.iterator();
                                Integer iter = 0;
                                while(i.hasNext()) {
                                    Number val = (Number) i.next();
                                    if ( iter.equals(0))
                                        minlong = val;
                                    if ( iter.equals(1))
                                        minlat = val;
                                    if ( iter.equals(2))
                                        maxlong = val;
                                    if ( iter.equals(3))
                                        maxlat = val;
                                    iter +=1;
                                }
                            }

                        }
                    }
                }
            }
        }

        model.addAttribute("polygon", "0");
        model.addAttribute("minlong", minlong);
        model.addAttribute("minlat", minlat);
        model.addAttribute("maxlong", maxlong);
        model.addAttribute("maxlat", maxlat);
        model.addAttribute("image32", internaltifffloat32path);
        model.addAttribute("imageauto", internaltiffautopath);
        model.addAttribute("json32", internaljsonfloat32path);
        model.addAttribute("jsonauto", internaljsonautopath);
        model.addAttribute("idprev", id);

        return "/visor/imagesprevlucasv1";
    }

    @GetMapping("/visor/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = storageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/visor/images/delete/{filename:.+}")
    public String deleteImage(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean existed = storageService.delete(filename);

            if (existed) {
                redirectAttributes.addFlashAttribute("message", "Delete the image successfully: " + filename);
            } else {
                redirectAttributes.addFlashAttribute("message", "The image does not exist!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Could not delete the image: " + filename + ". Error: " + e.getMessage());
        }

        return "redirect:/visor/images";
    }

    @GetMapping("/visor/image/pp2")
    public String pp2(){
        return "/visor/pp2";
    }
    @GetMapping("/visor/image/pp3")
    public String pp3(){
        return "/visor/pp3";
    }
}