package com.example.jpa_formacion.web.controller;

import com.example.jpa_formacion.config.details.SuperCustomerUserDetails;
import com.example.jpa_formacion.dto.*;
import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import java.util.ResourceBundle;


import java.util.*;

@Controller
public class AppUsuariosController extends AbstractController <UsuarioDto> {

    private final  UsuarioService service;
    private final RoleService roleService;

    private final GrupoService grupoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AppUsuariosController(MenuService menuService, UsuarioService service, RoleService roleService, GrupoService grupoService) {
        super(menuService);
        this.service = service;
        this.roleService = roleService;
        this.grupoService = grupoService;
    }
    @GetMapping("/")
    public String vistaHome( ModelMap interfazConPantalla){

        String  userName = "no informado";
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }

        interfazConPantalla.addAttribute("menuList", this.menuService.getMenuForEmail(userName));
        return "index";
    }
    @GetMapping("/privacy")
    public String vistaPrivacy( ModelMap interfazConPantalla){

        String  userName = "no informado";
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }

        interfazConPantalla.addAttribute("menuList", this.menuService.getMenuForEmail(userName));
        return "privacy";
    }
    @GetMapping("/termsandconditions")
    public String vistaTermAndConditions( ModelMap interfazConPantalla){

        String  userName = "no informado";
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        interfazConPantalla.addAttribute("menuList", this.menuService.getMenuForEmail(userName));
        return "termsandconditions";
    }
    @GetMapping("/project")
    public String vistaProject( ModelMap interfazConPantalla){

        String  userName = "no informado";
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        interfazConPantalla.addAttribute("menuList", this.menuService.getMenuForEmail(userName));
        return "projectoeuropeo";
    }
    @GetMapping("/usuarios")
    public String vistaUsuarios(@RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                    ModelMap interfazConPantalla){


        //tenemos que leer la lista de usuarios
        //Que elemento me la ofrece?
        //listaUsrTodos
        //List<UsuarioDto>  lusrdto = this.service.listaUsrTodos();
        //interfazConPantalla.addAttribute("listausuarios", lusrdto);
        //Obetenemos el objeto Page del servicio
        Integer pagina = 1;
        if (page.isPresent()) {
            pagina = page.get() -1;
        }
        Integer maxelementos = 10;
        if (size.isPresent()) {
            maxelementos = size.get();
        }
        Page<UsuarioDto> usuarioDtoPage =
                this.service.buscarTodos(PageRequest.of(pagina,maxelementos));
        System.out.println("Elementos encontrados en vistaUsuarios");
        System.out.println(usuarioDtoPage);
        interfazConPantalla.addAttribute(pageNumbersAttributeKey,dameNumPaginas(usuarioDtoPage));
        interfazConPantalla.addAttribute("listausuarios", usuarioDtoPage);
        return "usuarios/listausuariospagina";
    }

    @GetMapping("/usuarios/{idusr}")
    @PostAuthorize("hasRole('ADMIN')")
    public String vistaDatosUsuario(@PathVariable("idusr") Integer id, ModelMap interfazConPantalla){

        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UsuarioDto> usuarioDto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (usuarioDto.isPresent()){
            //Como encontré datos, obtengo el objerto de tipo "UsuarioDto"
            //addAttribute y thymeleaf no  entienden Optional
            UsuarioDto attr = usuarioDto.get();
            //Asigno atributos y muestro
            interfazConPantalla.addAttribute("datosUsuario",attr);

            return "usuarios/edit";
        } else{
            //Mostrar página usuario no existe
            return "usuarios/detallesusuarionoencontrado";
        }
    }


    @PostMapping("/usuarios/{idusr}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarDatosUsuario(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UsuarioDto> usuarioDto = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (usuarioDto.isPresent()){
            this.service.eliminarPorId(id);
            //Mostrar listado de usuarios
            return "redirect:/usuarios";
        } else{
            //Mostrar página usuario no existe
            return "usuarios/detallesusuarionoencontrado";
        }
    }
    @PostMapping("/usuarios/{idusr}/habilitar")
    @PreAuthorize("hasRole('ADMIN')")
    public String habilitarDatosUsuario(@PathVariable("idusr") Integer id){
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<Usuario> usuario = this.service.encuentraPorIdEntity(id);
        //¿Debería comprobar si hay datos?
        if (usuario.isPresent()){
            Usuario attr = usuario.get();
            if (attr.isActive())
                attr.setActive(false);
            else
                attr.setActive(true);
            this.service.getRepo().save(attr);
            //Mostrar listado de usuarios
            return "redirect:/usuarios";
        } else{
            //Mostrar página usuario no existe
            return "usuarios/detallesusuarionoencontrado";
        }
    }


    //Me falta un postmaping para guardar
    @PostMapping("/usuarios/{idusr}")
    public String guardarEdicionDatosUsuario(@PathVariable("idusr") Integer id, UsuarioDto usuarioDtoEntrada) throws Exception {
        //Cuidado que la password no viene
        //Necesitamos copiar la información que llega menos la password
        //Con el id tengo que buscar el registro a nivel de entidad
        Optional<UsuarioDto> usuarioDtoControl = this.service.encuentraPorId(id);
        //¿Debería comprobar si hay datos?
        if (usuarioDtoControl.isPresent()){
            //LLamo al método del servicioi para guardar los datos
            UsuarioDto usuarioDtoGuardar =  new UsuarioDto();
            usuarioDtoGuardar.setId(id);
            usuarioDtoGuardar.setEmail(usuarioDtoEntrada.getEmail());
            usuarioDtoGuardar.setNombreUsuario(usuarioDtoEntrada.getNombreUsuario());
            //Obtenemos la password del sercio
            Optional<Usuario> usuario = service.encuentraPorIdEntity((int) usuarioDtoGuardar.getId());
            if(usuario.isPresent()){
                this.service.guardar(usuarioDtoGuardar,usuario.get().getPassword());
            }
            else {
                this.service.guardar(usuarioDtoGuardar);
            }
            return String.format("redirect:/usuarios/%s", id);
        } else {
            //Mostrar página usuario no existe
            return "usuarios/detallesusuarionoencontrado";
        }
    }

    //Controlador de Login
    @GetMapping("/usuarios/login")
    public String vistaLogin(){
        return "usuarios/login";
    }
    @PostMapping("/usuarios/login")
    public String validarPasswordPst(@ModelAttribute(name = "loginForm" ) LoginDto loginDto) {
        String usr = loginDto.getUsername();
        System.out.println("usr :" + usr);
        String password = loginDto.getPassword();
        System.out.println("pass :" + password);
        //¿es correcta la password?
        if (service.getRepo().repValidarPassword(usr, passwordEncoder.encode(password) ) > 0)
        {
            return "index";
        }else {
            return "usuarios/login";
        }
    }

    //Controlador de Login
    @GetMapping("/usuarios/hasOlvidadoTuPassword")
    public String  hasOlvidadoTuPassword(@RequestParam(value = "email", required = false) String email){

        if (email!= null) {

            Optional<Usuario> usuario = service.getRepo().findUsuarioByEmailAndActiveTrue(email);

            if (usuario.isPresent()) {
                String token = usuario.get().getToken();
                System.out.println("------------------------- --------------"+ email + " token: "  + token);

                Email correoCambioContrasenia = new Email();
                correoCambioContrasenia.setFrom("notificaciones@agestturnos.es");
                correoCambioContrasenia.setTo(email);
                correoCambioContrasenia.setSubject("WALGREEN change password request");
                correoCambioContrasenia.setContent("Click url to change password http://localhost:8092/usuarios/resetpass/" + email +"/" + token);

                emailService.sendMail(correoCambioContrasenia);

            } else {
                email=null;
                return "redirect:/usuarios/hasOlvidadoTuPassword";
            }

            return "usuarios/emailEnviadoParaCambioPass";
        }
        return "usuarios/hasolvidado";
    }

    // Controlador para Reset password o ¿Has olvidado tu contraseña?
    @GetMapping("/usuarios/resetpass/{email}/{token}")
    public String cambiopass(@PathVariable("email") String email, @PathVariable("token") String token, ModelMap intefrazConPantalla) {
        Optional<Usuario> usuario = service.getRepo().findUsuarioByEmailAndTokenAndActiveTrue(email,token );
        System.out.println(email + ":" + token );
        UsuarioDtoPsw usuarioCambioPsw = new UsuarioDtoPsw();

        if (usuario.isPresent()){
            usuarioCambioPsw.setEmail(usuario.get().getEmail());
            usuarioCambioPsw.setPassword("******************");
            usuarioCambioPsw.setNewpassword("******************");
            intefrazConPantalla.addAttribute("datos", usuarioCambioPsw);
            return "usuarios/resetearpasswordlogin";
        }else {

            //Mostrar página usuario no existe
            return "usuarios/detallesusuarionoencontrado";
        }
    }

    @PostMapping("/usuarios/resetpass")
    public String saveListaUsuariuos(@ModelAttribute  UsuarioDtoPsw  dto,
                                     @ModelAttribute UsuarioDto usuarioDTO,
                                     @RequestParam(value = "lang", required = false) String lang,
                                     Model model) throws Exception {
        String language = "en";
        if (lang!= null) {
            language = lang;
        }
        var locale = new Locale(language);
        var messages = ResourceBundle.getBundle("messages", locale);
        //Si las password no coinciden a la pag de error
        if (dto.getPassword().equals(dto.getNewpassword())){
            //Buscamnos el usuario
            Usuario usuario = service.getRepo().findByEmailAndActiveTrue(dto.getEmail());
            //Actualizo la password despues de codificarla
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            //Guardo el usuario
            Usuario usuarioguarado = service.guardarEntidadEntidad(usuario);

            Email correoCambioContrasenia = new Email();
            correoCambioContrasenia.setFrom(messages.getString("principal.email.from"));
            correoCambioContrasenia.setTo(usuarioDTO.getEmail());
            correoCambioContrasenia.setSubject(messages.getString("principal.email.subject1"));
            correoCambioContrasenia.setContent(messages.getString("principal.email.content1") + usuarioDTO.getEmail() );

            emailService.sendMail(correoCambioContrasenia);

            return "redirect:/login";
        }else {

            /// Si las pass no coinciden
            //model.addAttribute("error", true);
            //return "/resetpass";
            return "usuarios/resetearpasswordlogin";

        }
    }


    //Controlador de cambio de password
    @GetMapping("/usuarios/cambiopass")
    public String vistaCambiopasword( ModelMap intefrazConPantalla){
        CambioPswDto cambioPswDto = new CambioPswDto();
        intefrazConPantalla.addAttribute("datos", cambioPswDto);
        return "usuarios/cambiopassword";
    }
    @PostMapping("/usuarios/cambiopass")
    public String cambioPasswordPst(@ModelAttribute(name="datos")CambioPswDto cambioPswDto) throws Exception {
        //Obtenemos los datos del usuario
        Integer userId = ((SuperCustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserID();
        Optional<Usuario> usuario = service.encuentraPorIdEntity(userId);
        if (usuario.isPresent()){
            Usuario usuariomod = usuario.get();
            //Encriptamos las passwords
            System.out.println("/usuarios/cambiopass ant:" + cambioPswDto.getPasswordant() + "---- Nueva:" + cambioPswDto.getPasswordnueva() );
            String passwordAnt =  passwordEncoder.encode(cambioPswDto.getPasswordant());
            String passwordNueva =  passwordEncoder.encode(cambioPswDto.getPasswordnueva());
            System.out.println("/usuarios/cambiopass ant:" + passwordAnt );
            //Modificicamos la passsword
            usuariomod.setPassword(passwordNueva);
            System.out.println("/usuarios/cambiopass antes de guardar ");
            //Guardamos el usuario
            Usuario usuario1 = service.guardarEntidadEntidad(usuariomod);
            return "redirect:/logout";
        }
        else {
            return "usuarios/cambiopassword";
        }

    }


    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(  WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage() ;
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "usuarios/login";
    }


}
