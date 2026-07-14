package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.streetwear.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}
