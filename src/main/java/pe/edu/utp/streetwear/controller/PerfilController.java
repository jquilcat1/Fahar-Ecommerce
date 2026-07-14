package pe.edu.utp.streetwear.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.streetwear.model.Usuario;
import pe.edu.utp.streetwear.repository.UsuarioRepository;
import pe.edu.utp.streetwear.service.TransaccionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PerfilController {

    private final TransaccionService transaccionService;
    private final UsuarioRepository usuarioRepository; 

    @GetMapping("/perfil")
    public String verPerfil(Principal principal, Model model) {
        if (principal != null) {
            String correoLogueado = principal.getName(); 
            Usuario usuario = usuarioRepository.findByCorreo(correoLogueado)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            model.addAttribute("usuario", usuario);
        }
        return "perfil/perfil"; 
    }

    @PostMapping("/perfil/editar")
    public String actualizarPerfil(Principal principal, 
                                   @RequestParam String nombre, 
                                   @RequestParam String apellidos, 
                                   @RequestParam String telefono) {
        
        if (principal != null) {
            String correoLogueado = principal.getName();
            Usuario usuario = usuarioRepository.findByCorreo(correoLogueado)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setTelefono(telefono);
            
            usuarioRepository.save(usuario);
        }
        
        return "redirect:/perfil"; 
    }

    @GetMapping("/historial")
    public String verHistorial(Principal principal, Model model) {
        if (principal != null) {
            String correoLogueado = principal.getName();
            Usuario usuario = usuarioRepository.findByCorreo(correoLogueado)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            model.addAttribute("pedidos", transaccionService.listarPedidosPorUsuario(usuario.getId()));
        }
        return "perfil/historial";
    }
}