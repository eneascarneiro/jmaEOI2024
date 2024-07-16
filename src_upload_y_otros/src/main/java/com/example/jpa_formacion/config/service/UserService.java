package com.example.jpa_formacion.config.service;



import com.example.jpa_formacion.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * The interface User service.
 */
@Service
public interface UserService {
    /**
     * Save user.
     *
     * @param usuario model
     */
    public String getEncodedPassword(Usuario usuario);
}
