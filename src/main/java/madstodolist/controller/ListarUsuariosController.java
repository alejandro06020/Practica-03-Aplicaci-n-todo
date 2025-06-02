package madstodolist.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
