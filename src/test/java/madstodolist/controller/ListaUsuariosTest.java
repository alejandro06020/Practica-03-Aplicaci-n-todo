package madstodolist.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class ListaUsuariosTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioService usuarioService;
    
    // Método para inicializar los datos de prueba en la BD
    // Devuelve el identificador del usuario de la BD
    Long addUsuarioBD() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        UsuarioData nuevoUsuario = usuarioService.registrar(usuario);
        return nuevoUsuario.getId();
    }
    
    //Test para probar el controlador para la pagina de registrados
    @Test
    public void listaUsuarioController() throws Exception {
    	// GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();
        
        // WHEN, THEN
        // se realiza la petición GET al listado de usuarios,
        // el HTML devuelto contiene la lista de usuarios.
        String url = "/registrados";
        this.mockMvc.perform(get(url))
                .andExpect(content().string(containsString("user@ua")));
    }
    //Test para probar la pagina de descripcion de usuario
    @Test
    public void descripcionUsuarioGet() throws Exception {
    	// GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();
        // WHEN, THEN
        // se realiza la petición GET a la descripcion del usuario,
        // el HTML devuelto contiene los datos del usuario.
        String url = "/registrados/"+usuarioId.toString();
        this.mockMvc.perform(get(url)).andExpect(content().string(
        		allOf(containsString("Usuario Ejemplo"),containsString("user@ua"))));
    }
}
