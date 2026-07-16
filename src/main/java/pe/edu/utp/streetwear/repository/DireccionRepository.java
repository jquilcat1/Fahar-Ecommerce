package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.streetwear.model.Direccion;
import java.util.List;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByUsuarioId(Long usuarioId);
}
