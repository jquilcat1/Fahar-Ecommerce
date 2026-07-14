package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RolDTO {
    private Integer id;

    @NotBlank(message = "El nombre del rol es obligatorio.")
    private String nombre;
}