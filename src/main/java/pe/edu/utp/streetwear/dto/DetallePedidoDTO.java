package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetallePedidoDTO {
    private Long id;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "Debe comprar al menos 1 unidad.")
    private Integer cantidad;

    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Long pedidoId;
    private Long productoId;
    private String nombreProducto;
}