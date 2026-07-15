package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres.")
    private String nombre;

    @NotBlank(message = "Debe proporcionar una descripción del producto.")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @Positive(message = "El precio debe ser mayor a 0.")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser un número negativo.")
    private Integer stock;

    // --- NUEVOS CAMPOS PARA RECIBIR ARCHIVOS FÍSICOS DEL FORMULARIO ---
    private MultipartFile archivo1;
    private MultipartFile archivo2;
    private MultipartFile archivo3;
    private MultipartFile archivo4;

    // --- CAMPOS DE TEXTO PARA ALMACENAR LA RUTA GENERADA ---
    private String imagenUrl;
    private String imagenUrl2;
    private String imagenUrl3;
    private String imagenUrl4;

    @NotNull(message = "Debe seleccionar una categoría.")
    private Integer categoriaId;

    private String nombreCategoria;

    @NotNull(message = "Debe seleccionar una marca (ej. Yeezy).")
    private Integer marcaId;

    private String nombreMarca;
}
