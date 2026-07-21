package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.streetwear.dto.ItemCarrito;
import pe.edu.utp.streetwear.dto.PedidoDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.TransaccionMapper;
import pe.edu.utp.streetwear.model.DetallePedido;
import pe.edu.utp.streetwear.model.Pago;
import pe.edu.utp.streetwear.model.Pedido;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.model.Usuario;
import pe.edu.utp.streetwear.repository.DetallePedidoRepository;
import pe.edu.utp.streetwear.repository.PagoRepository;
import pe.edu.utp.streetwear.repository.PedidoRepository;
import pe.edu.utp.streetwear.repository.ProductoRepository;
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

    // Inyecciones nuevas para el checkout real
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;

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

    // NUEVO: Método integrado para procesar la compra desde el carrito
    @Transactional
    public Pedido crearPedidoDesdeCarrito(List<ItemCarrito> carrito, Usuario usuario, String metodoPago,
            BigDecimal total) {

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(total);
        pedido.setEstado("PAGADO");
        pedido = pedidoRepository.save(pedido);

        for (ItemCarrito item : carrito) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setSubtotal(item.getSubtotal());
            detallePedidoRepository.save(detalle);
        }

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(total);
        pago.setEstado("COMPLETADO");
        pagoRepository.save(pago);

        return pedido;
    }
}