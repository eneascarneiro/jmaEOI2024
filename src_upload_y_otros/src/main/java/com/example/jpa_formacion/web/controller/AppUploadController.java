package com.example.jpa_formacion.web.controller;



import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.UploadFilesDto;
import com.example.jpa_formacion.dto.UsuarioDtoPsw;
import com.example.jpa_formacion.model.UploadedFiles;
import com.example.jpa_formacion.service.MenuService;
import com.example.jpa_formacion.service.UploadedFilesService;
import com.example.jpa_formacion.service.UsuarioService;
import com.example.jpa_formacion.util.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@SessionAttributes("productos")
@Controller
public class AppUploadController extends AbstractController <UploadFilesDto>  {

    private final UploadedFilesService service;

    private final UsuarioService usuarioService;

    public AppUploadController(MenuService menuService, UploadedFilesService service, UsuarioService usuarioService) {
        super(menuService);
        this.service = service;
        this.usuarioService = usuarioService;
    }


    @GetMapping("/upload")
    public String vistaGet(Model model ) throws IOException {
        //check content of user folder
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        List<String> results = new ArrayList<String>();
        UploadFilesDto uploadFilesDto = new UploadFilesDto();
        //Obtenemos los datos del usuario

        String uploadDir = "src/main/resources/static/files/src_user_data/" + userId.toString() + "/csvcoords";
        //check if user folder exists if not create
        Files.createDirectories(Paths.get(uploadDir));
        File[] files = new File(uploadDir).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        model.addAttribute("files",results);
        model.addAttribute("datos",uploadFilesDto);
        return "upload/upload";
    }


    @PostMapping("/upload")
    public String uploadPost(@RequestParam MultipartFile file, HttpSession session ,@ModelAttribute(name ="datos") UploadFilesDto uploadFilesDto, Model model) throws IOException {
        String path=session.getServletContext().getRealPath("/");
        String filename=file.getOriginalFilename();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();

        String uploadDir = "src/main/resources/static/files/src_user_data/" + userId.toString() + "/csvcoords";
        if (FileUploadUtil.checkfile(uploadDir,fileName )){
            model.addAttribute("file",file);
            model.addAttribute("description","File alredy exists.");
        }
        else {
            //check if user folder exists if not create
            Files.createDirectories(Paths.get(uploadDir));

            FileUploadUtil.saveFile(uploadDir, fileName, file);
            //Insertamos en la tabla de csv
            UploadedFiles uploadedFiles = new UploadedFiles();
            uploadedFiles.setUsuarioUpload(usuarioService.buscar(userId).get());
            uploadedFiles.setPath(uploadDir);
            uploadedFiles.setDescription(fileName);
            uploadedFiles.setShared(false);
            UploadedFiles uploadedFilessaved = service.getRepo().save(uploadedFiles);
            model.addAttribute("file",file);
        }

        //return new ModelAndView("upload/fileUploadView","filename",path+"/"+filename);
        return "upload/fileUploadViewUno";
    }
    @GetMapping("/uploadimg")
    public String vistaGetImg( ){

        return "upload/uploadimg";
    }
    @PostMapping("/uploadimg")
    public String uploadImgPost(@RequestParam MultipartFile file, HttpSession session , Model model) throws IOException {
        String path=session.getServletContext().getRealPath("/");
        String filename=file.getOriginalFilename();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());


        String uploadDir = "src/main/resources/static/imagenes/";

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        model.addAttribute("file",file);
        //return new ModelAndView("upload/fileUploadView","filename",path+"/"+filename);
        return "upload/fileUploadViewUno";
    }
}
