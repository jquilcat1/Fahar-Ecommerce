package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.streetwear.dto.UsuarioDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.UsuarioMapper;
import pe.edu.utp.streetwear.model.Usuario;
import pe.edu.utp.streetwear.repository.RolRepository;
import pe.edu.utp.streetwear.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    // MÉTODO AGREGADO: Devuelve la entidad Usuario buscando por su correo
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + email));
    }

    public UsuarioDTO guardarUsuario(UsuarioDTO dto) {
        Usuario usuario = usuarioMapper.toModel(dto);

        Integer rolId = (dto.getRolId() != null) ? dto.getRolId() : 2;
        usuario.setRol(rolRepository.findById(rolId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no válido")));

        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }
}