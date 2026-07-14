package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoDTO {
    private Long id;

    @NotBlank(message = "El método de pago es obligatorio.")
    private String metodoPago;

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto debe ser mayor a 0.")
    private BigDecimal monto;

    private LocalDateTime fechaPago;
    private String estado;

    @NotNull(message = "El pago debe estar asociado a un pedido.")
    private Long pedidoId;
}