package com.example.jpa_formacion.config.service;


import com.example.jpa_formacion.model.Usuario;
import com.example.jpa_formacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User service.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public String getEncodedPassword(Usuario usuario) {
        String passwd = usuario.getPassword();
        String encodedPasswod = passwordEncoder.encode(passwd);
        return encodedPasswod;
    }
}