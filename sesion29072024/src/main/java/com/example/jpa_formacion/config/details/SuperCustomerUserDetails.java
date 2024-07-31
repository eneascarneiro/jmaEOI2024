package com.example.jpa_formacion.config.details;

import com.example.jpa_formacion.model.GrupoTrabajo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The type Super customer user details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperCustomerUserDetails implements UserDetails {


   //Principales atributos de la clase
    private String username;
    private String password;


    private String coper_username;
    private String coper_password;
    private String coper_client_id;
    private String coper_client_secret;
    private Integer userID;

    private boolean isActive=true;
    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isCopernicusLogged=false;
    // Carrito
    List<GrupoTrabajo> grupos = new ArrayList<>();
    //Permisos
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Instantiates a new Custom user details.
     *
     * @param username    the username
     * @param password    the password
     * @param name        the name
     * @param authorities the authorities
     * @param edad        the edad
     * @param email       the email
     * @param userID      the user id
     */


    @Override
    public boolean isEnabled() {
        return true;
    }
}
