package madstodolist.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import madstodolist.dto.UsuarioData;
import madstodolist.model.Usuario;
import madstodolist.repository.UsuarioRepository;
import madstodolist.service.UsuarioService;

@Controller
public class ListarUsuariosController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/registrados")
	public String listadoUsuarios(Model model, HttpSession session) {
		List<UsuarioData> usuarios = usuarioService.getAll();
		model.addAttribute("usuarios", usuarios);
		return "listaUsuarios";
	}
	@GetMapping("/registrados/{id}")
	public String descripcionUsuario(@PathVariable(value="id") Long idUsuario,Model model, HttpSession session) {
		UsuarioData usuario = usuarioService.findById(idUsuario);
		model.addAttribute("usuario",usuario);
		return "descripcionUsuario";
	}
}
