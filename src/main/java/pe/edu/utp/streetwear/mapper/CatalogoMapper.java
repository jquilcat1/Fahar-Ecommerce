package pe.edu.utp.streetwear.mapper;

import org.springframework.stereotype.Component;
import pe.edu.utp.streetwear.dto.*;
import pe.edu.utp.streetwear.model.*;

@Component
public class CatalogoMapper {
    public ProductoDTO toDTO(Producto model) {
        if (model == null)
            return null;
        ProductoDTO dto = new ProductoDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        dto.setPrecio(model.getPrecio());
        dto.setStock(model.getStock());

        // --- MAPEO DE LA GALERÍA COMPLETA (BD a Vista) ---
        dto.setImagenUrl(model.getImagenUrl());
        dto.setImagenUrl2(model.getImagenUrl2());
        dto.setImagenUrl3(model.getImagenUrl3());
        dto.setImagenUrl4(model.getImagenUrl4());

        // Mapeamos ID y Nombre de la Categoría
        if (model.getCategoria() != null) {
            dto.setCategoriaId(model.getCategoria().getId());
            dto.setNombreCategoria(model.getCategoria().getNombre());
        }

        // Mapeamos ID y Nombre de la Marca
        if (model.getMarca() != null) {
            dto.setMarcaId(model.getMarca().getId());
            dto.setNombreMarca(model.getMarca().getNombre());
        }
        return dto;
    }

    public Producto toModel(ProductoDTO dto) {
        if (dto == null)
            return null;
        Producto model = new Producto();
        model.setId(dto.getId());
        model.setNombre(dto.getNombre());
        model.setDescripcion(dto.getDescripcion());
        model.setPrecio(dto.getPrecio());
        model.setStock(dto.getStock());

        // --- MAPEO DE LA GALERÍA COMPLETA (Vista a BD) ---
        model.setImagenUrl(dto.getImagenUrl());
        model.setImagenUrl2(dto.getImagenUrl2());
        model.setImagenUrl3(dto.getImagenUrl3());
        model.setImagenUrl4(dto.getImagenUrl4());

        return model;
    }

    public MarcaDTO toDTO(Marca model) {
        if (model == null)
            return null;
        MarcaDTO dto = new MarcaDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        return dto;
    }

    public CategoriaDTO toDTO(Categoria model) {
        if (model == null)
            return null;
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        return dto;
    }
}