package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.streetwear.dto.DireccionDTO;
import pe.edu.utp.streetwear.dto.ResenaDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.InteraccionMapper;
import pe.edu.utp.streetwear.model.Direccion;
import pe.edu.utp.streetwear.model.Resena;
import pe.edu.utp.streetwear.repository.DireccionRepository;
import pe.edu.utp.streetwear.repository.ProductoRepository;
import pe.edu.utp.streetwear.repository.ResenaRepository;
import pe.edu.utp.streetwear.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteraccionService {

    private final DireccionRepository direccionRepository;
    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InteraccionMapper interaccionMapper;

    public List<DireccionDTO> listarDireccionesUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId).stream()
                .map(interaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DireccionDTO guardarDireccion(DireccionDTO dto) {
        Direccion direccion = interaccionMapper.toModel(dto);
        direccion.setUsuario(usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));
        
        return interaccionMapper.toDTO(direccionRepository.save(direccion));
    }

    public List<ResenaDTO> listarResenasPorProducto(Long productoId) {
        return resenaRepository.findByProductoId(productoId).stream()
                .map(interaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ResenaDTO guardarResena(ResenaDTO dto) {
        Resena resena = interaccionMapper.toModel(dto);
        
        resena.setUsuario(usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));
        resena.setProducto(productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado para la reseña")));

        return interaccionMapper.toDTO(resenaRepository.save(resena));
    }
}