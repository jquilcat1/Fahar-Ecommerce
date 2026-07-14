package pe.edu.utp.streetwear.mapper;

import org.springframework.stereotype.Component;
import pe.edu.utp.streetwear.dto.UsuarioDTO;
import pe.edu.utp.streetwear.model.Usuario;

@Component
public class UsuarioMapper {
    public UsuarioDTO toDTO(Usuario model) {
        if (model == null)
            return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setApellidos(model.getApellidos());
        dto.setCorreo(model.getCorreo());
        dto.setTelefono(model.getTelefono());
        if (model.getRol() != null)
            dto.setRolId(model.getRol().getId());
        return dto;
    }

    public Usuario toModel(UsuarioDTO dto) {
        if (dto == null)
            return null;
        Usuario model = new Usuario();
        model.setId(dto.getId());
        model.setNombre(dto.getNombre());
        model.setApellidos(dto.getApellidos());
        model.setCorreo(dto.getCorreo());
        model.setPassword(dto.getPassword());
        model.setTelefono(dto.getTelefono());
        return model;
    }
}