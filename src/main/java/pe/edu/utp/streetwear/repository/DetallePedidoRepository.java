package pe.edu.utp.streetwear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.streetwear.model.DetallePedido;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedidoId(Long pedidoId);
}
