package pe.edu.utp.streetwear.mapper;

import org.springframework.stereotype.Component;
import pe.edu.utp.streetwear.dto.*;
import pe.edu.utp.streetwear.model.*;

@Component
public class InteraccionMapper {

    // --- DIRECCION ---
    public DireccionDTO toDTO(Direccion model) {
        if (model == null) return null;
        DireccionDTO dto = new DireccionDTO();
        dto.setId(model.getId());
        dto.setCalle(model.getCalle());
        dto.setDistrito(model.getDistrito()); 
        dto.setCodigoPostal(model.getCodigoPostal());
        dto.setReferencia(model.getReferencia());
        if (model.getUsuario() != null) dto.setUsuarioId(model.getUsuario().getId());
        return dto;
    }

    public Direccion toModel(DireccionDTO dto) {
        if (dto == null) return null;
        Direccion model = new Direccion();
        model.setId(dto.getId());
        model.setCalle(dto.getCalle());
        model.setDistrito(dto.getDistrito());
        model.setCodigoPostal(dto.getCodigoPostal());
        model.setReferencia(dto.getReferencia());
        return model;
    }

    // --- RESENA ---
    public ResenaDTO toDTO(Resena model) {
        if (model == null) return null;
        ResenaDTO dto = new ResenaDTO();
        dto.setId(model.getId());
        dto.setCalificacion(model.getCalificacion());
        dto.setComentario(model.getComentario());
        dto.setFecha(model.getFecha());
        if (model.getUsuario() != null) {
            dto.setUsuarioId(model.getUsuario().getId());
            dto.setNombreUsuario(model.getUsuario().getNombre());
        }
        if (model.getProducto() != null) dto.setProductoId(model.getProducto().getId());
        return dto;
    }

    public Resena toModel(ResenaDTO dto) {
        if (dto == null) return null;
        Resena model = new Resena();
        model.setId(dto.getId());
        model.setCalificacion(dto.getCalificacion());
        model.setComentario(dto.getComentario());
        return model;
    }
}