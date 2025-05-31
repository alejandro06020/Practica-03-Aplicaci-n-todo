package madstodolist.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;

@Controller
public class HomeController {
	@Autowired
	UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }
    //Esta pagina about se mostrara a los usuarios no logeados
    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }
    //Creamos una nueva pagina about para usuarios logeados
    @GetMapping("/usuarios/{id}/aboutIn")
    public String aboutIn(@PathVariable(value="id") Long idUsuario,Model model,HttpSession session) {
    	comprobarUsuarioLogeado(idUsuario);
    	UsuarioData usuario = usuarioService.findById(idUsuario);
    	model.addAttribute("usuario", usuario);
    	return "aboutIn";
    }
}
