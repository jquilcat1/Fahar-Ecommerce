package pe.edu.utp.streetwear.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.streetwear.model.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Page<Producto> findByCategoriaIdInAndMarcaIdIn(List<Integer> categorias, List<Integer> marcas, Pageable pageable);
    Page<Producto> findByCategoriaIdIn(List<Integer> categorias, Pageable pageable);
    Page<Producto> findByMarcaIdIn(List<Integer> marcas, Pageable pageable);
}