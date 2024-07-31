package com.example.jpa_formacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    String to;
    String from;
    String subject;
    String content;

    private Map<String, Object> model;
}
