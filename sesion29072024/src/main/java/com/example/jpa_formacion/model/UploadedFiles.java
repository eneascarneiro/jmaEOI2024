package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "uploadedfiles")
public class UploadedFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean shared = true;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioUpload;

}
