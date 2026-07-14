package pe.edu.utp.streetwear.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private String estado;
    private Long usuarioId;

    private List<DetallePedidoDTO> detalles;
}