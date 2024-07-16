package com.example.jpa_formacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements Serializable {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column (name ="email",length = 50)
    private String email;
    @Column (name ="nombre_usuario",length = 30)
    private String nombreUsuario;
    @Column (name ="password",length = 250)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "codigoGrupoTrabajo")
    private GrupoTrabajo grupoTrabajo;


    @OneToMany(mappedBy = "usuarioUpload" )
    private Set<UploadedFiles> uploadedFiles;

    @OneToMany(mappedBy = "usuarioScript" )
    private Set<EvalScript> evalScripts;

    @OneToMany(mappedBy = "usuarioPythonScript" )
    private Set<PythonScript> pythonScripts;

    @OneToMany(mappedBy = "usuarioFiltro" )
    private Set<FiltroListarArchivos> filtroListarArchivos;

    @Basic(optional = false)
    private boolean active = true;
    // Utilizamos  UUID.randomUUID de java util para generar el token de forma aleatoria cada vez que se cree un usuario
    // el token lo utilizaremos en el reset password cuando enviamos el correo al usuario para el cambio de pass
    private String token = UUID.randomUUID().toString();


}
