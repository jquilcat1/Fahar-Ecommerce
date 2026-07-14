package pe.edu.utp.streetwear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direcciones")
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false, length = 100)
    private String distrito;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
