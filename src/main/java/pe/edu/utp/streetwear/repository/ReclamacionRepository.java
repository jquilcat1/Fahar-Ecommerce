package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.streetwear.model.Reclamacion;

public interface ReclamacionRepository extends JpaRepository<Reclamacion, Long> {
}