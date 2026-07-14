package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaDTO {
    private Integer id;

    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    private String descripcion;
}