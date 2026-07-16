package pe.edu.utp.streetwear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito {
    private Long productoId;
    private String nombre;
    private String marca;
    private BigDecimal precio; // Cambiado a BigDecimal
    private String talla;
    private Integer cantidad;
    private String imagenUrl;
    
    // Método para calcular el subtotal usando aritmética de BigDecimal
    public BigDecimal getSubtotal() {
        if (this.precio == null || this.cantidad == null) {
            return BigDecimal.ZERO;
        }
        return this.precio.multiply(BigDecimal.valueOf(this.cantidad));
    }
}