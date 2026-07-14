package pe.edu.utp.streetwear.mapper;

import org.springframework.stereotype.Component;
import pe.edu.utp.streetwear.dto.*;
import pe.edu.utp.streetwear.model.*;

@Component
public class TransaccionMapper {
    public PedidoDTO toDTO(Pedido model) {
        if (model == null)
            return null;
        PedidoDTO dto = new PedidoDTO();
        dto.setId(model.getId());
        dto.setFechaPedido(model.getFechaPedido());
        dto.setTotal(model.getTotal());
        dto.setEstado(model.getEstado());
        if (model.getUsuario() != null)
            dto.setUsuarioId(model.getUsuario().getId());
        return dto;
    }

    public DetallePedidoDTO toDTO(DetallePedido model) {
        if (model == null)
            return null;
        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setId(model.getId());
        dto.setCantidad(model.getCantidad());
        dto.setPrecioUnitario(model.getPrecioUnitario());
        dto.setSubtotal(model.getSubtotal());
        if (model.getPedido() != null)
            dto.setPedidoId(model.getPedido().getId());
        if (model.getProducto() != null) {
            dto.setProductoId(model.getProducto().getId());
            dto.setNombreProducto(model.getProducto().getNombre());
        }
        return dto;
    }

    public PagoDTO toDTO(Pago model) {
        if (model == null)
            return null;
        PagoDTO dto = new PagoDTO();
        dto.setId(model.getId());
        dto.setMetodoPago(model.getMetodoPago());
        dto.setMonto(model.getMonto());
        dto.setFechaPago(model.getFechaPago());
        dto.setEstado(model.getEstado());
        if (model.getPedido() != null)
            dto.setPedidoId(model.getPedido().getId());
        return dto;
    }
}