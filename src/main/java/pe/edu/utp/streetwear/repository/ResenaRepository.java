package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.streetwear.model.Resena;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
}