package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.streetwear.model.Resena;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
}