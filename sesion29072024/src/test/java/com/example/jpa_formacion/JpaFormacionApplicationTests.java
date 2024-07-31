package com.example.jpa_formacion;

import com.example.jpa_formacion.dto.UsuarioDtoPsw;
import com.example.jpa_formacion.model.GrupoTrabajo;
import com.example.jpa_formacion.model.Role;
import com.example.jpa_formacion.service.GrupoService;
import com.example.jpa_formacion.service.RoleService;
import com.example.jpa_formacion.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class JpaFormacionApplicationTests {
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	RoleService roleService;
	@Autowired
	GrupoService grupoService;
	void crearroles() throws Exception {
	}
	void crearusuario() throws Exception {
		/*Creamos el administrador*/
		UsuarioDtoPsw usuarioDto = new UsuarioDtoPsw();
		usuarioDto.setNombreUsuario("Juan");
		usuarioDto.setEmail("juan@juan.com");
		usuarioDto.setNombreEmail("Juan Aroca");
		usuarioDto.setPassword("$2a$10$WQXI1GT5t7Eq6/RAOUNvUejQEpvle2xEHJqKAiVTztg3jivCOP2LS");
		/* establecemos el grupo de trabajo */
		Optional<GrupoTrabajo> grupoTrabajo = grupoService.buscar(1);
		if (grupoTrabajo.isPresent()) {
			usuarioDto.setGrupoTrabajo(grupoTrabajo.get());
		}
		/* establecemos el role */
		Optional<Role> role = roleService.buscar(1L);
		Set<Role> roleSet = new HashSet<>();
		if (role.isPresent()){
			roleSet.add(role.get());
			usuarioDto.setRoles(roleSet);
		}
		/* grabamos el resultado */
		usuarioService.guardar(usuarioDto);
		/*Creamos el agricultor*/
		UsuarioDtoPsw usuarioDto1 = new UsuarioDtoPsw();
		usuarioDto1.setNombreUsuario("Luis");
		usuarioDto1.setEmail("luis@juan.com");
		usuarioDto1.setNombreEmail("Luis Aroca");
		usuarioDto1.setPassword("$2a$10$WQXI1GT5t7Eq6/RAOUNvUejQEpvle2xEHJqKAiVTztg3jivCOP2LS");
		/* establecemos el grupo de trabajo */
		Optional<GrupoTrabajo> grupoTrabajo1 = grupoService.buscar(1);
		if (grupoTrabajo1.isPresent()) {
			usuarioDto.setGrupoTrabajo(grupoTrabajo1.get());
		}
		/* establecemos el role */
		Optional<Role> role1 = roleService.buscar(2L);
		Set<Role> roleSet1 = new HashSet<>();
		if (role1.isPresent()){
			roleSet1.add(role1.get());
			usuarioDto.setRoles(roleSet1);
		}

	}
	@Test
	public void initialization() throws Exception {
		crearusuario();
	}


}
