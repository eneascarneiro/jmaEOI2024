package com.example.jpa_formacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Copernicuscredentials {

    private String clientid;

    private String secret;

    private String id;

    private String token;
}
