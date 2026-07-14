package pe.edu.utp.streetwear.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DireccionDTO {
    private Long id;

    @NotBlank(message = "La calle es obligatoria.")
    private String calle;

    @NotBlank(message = "El distrito es obligatorio.")
    private String distrito;

    private String codigoPostal;
    private String referencia;

    @NotNull(message = "La dirección debe pertenecer a un usuario.")
    private Long usuarioId;
}