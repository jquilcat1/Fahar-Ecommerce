package pe.edu.utp.streetwear.repository;

import pe.edu.utp.streetwear.model.MensajeContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    // 1. Te devuelve todos los mensajes que tengan un estado específico (ej. "PENDIENTE")
    List<MensajeContacto> findByEstado(String estado);

    // 2. Te devuelve todos los mensajes de un tipo específico (ej. "reclamo" para ver solo el Libro de Reclamaciones)
    List<MensajeContacto> findByAsunto(String asunto);

    // 3. Puedes combinarlos: Traer todos los reclamos que aún están pendientes
    List<MensajeContacto> findByAsuntoAndEstado(String asunto, String estado);
}