package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.streetwear.model.Marca;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    Optional<Marca> findByNombre(String nombre);
}
