package pe.edu.utp.streetwear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl; // Foto 1 (Principal)

    @Column(name = "imagen_url_2", length = 255)
    private String imagenUrl2; // Foto 2 (Hover/Secundaria)

    @Column(name = "imagen_url_3", length = 255)
    private String imagenUrl3; // Foto 3 (Opcional)

    @Column(name = "imagen_url_4", length = 255)
    private String imagenUrl4; // Foto 4 (Opcional)

    // Relación con Categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relación con Marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;
}
