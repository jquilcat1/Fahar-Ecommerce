package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.streetwear.dto.PedidoDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.TransaccionMapper;
import pe.edu.utp.streetwear.model.Pedido;
import pe.edu.utp.streetwear.repository.PedidoRepository;
import pe.edu.utp.streetwear.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransaccionMapper transaccionMapper;

    public List<PedidoDTO> listarPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO crearPedido(PedidoDTO dto) {
        Pedido pedido = new Pedido();

        pedido.setTotal(dto.getTotal() != null ? dto.getTotal() : BigDecimal.ZERO); 
        pedido.setEstado("PENDIENTE");

        pedido.setUsuario(usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado para generar pedido")));

        Pedido guardado = pedidoRepository.save(pedido);
        return transaccionMapper.toDTO(guardado);
    }
}