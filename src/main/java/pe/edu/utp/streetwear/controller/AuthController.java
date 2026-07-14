package pe.edu.utp.streetwear.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.edu.utp.streetwear.dto.UsuarioDTO;
import pe.edu.utp.streetwear.service.UsuarioService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "auth/registro";
        }
        usuarioService.guardarUsuario(usuarioDTO);
        return "redirect:/login?exito";
    }
}