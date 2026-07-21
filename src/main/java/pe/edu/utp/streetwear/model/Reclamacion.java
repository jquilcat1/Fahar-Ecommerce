package pe.edu.utp.streetwear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reclamacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(name = "tipo_bien", length = 50)
    private String tipoBien;

    @Column(name = "monto_reclamado")
    private Double montoReclamado;

    @Column(name = "detalle_bien", length = 255)
    private String detalleBien;

    @Column(name = "tipo_reclamo", nullable = false, length = 50)
    private String tipoReclamo;

    @Column(name = "detalle_reclamo", nullable = false, columnDefinition = "TEXT")
    private String detalleReclamo;

    @Column(name = "pedido_consumidor", nullable = false, columnDefinition = "TEXT")
    private String pedidoConsumidor;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false, length = 20)
    private String estado;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}