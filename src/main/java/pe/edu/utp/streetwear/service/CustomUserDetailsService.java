package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTACIÓN NUEVA
import pe.edu.utp.streetwear.model.Usuario;
import pe.edu.utp.streetwear.repository.UsuarioRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true) 
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("El correo no existe"));

        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().getNombre())));
    }
}