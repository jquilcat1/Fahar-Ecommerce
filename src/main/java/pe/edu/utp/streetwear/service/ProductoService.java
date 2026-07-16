package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.repository.ProductoRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Page<Producto> listarProductosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findAll(pageable);
    }

    public Page<Producto> listarProductosFiltrados(int page, int size, List<Integer> categorias, List<Integer> marcas,
            String sortStr) {

        // 1. Configurar el Ordenamiento (Por defecto: Nuevos Ingresos)
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort
                .by(org.springframework.data.domain.Sort.Direction.DESC, "id");
        if ("1".equals(sortStr)) {
            sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC,
                    "precio");
        } else if ("2".equals(sortStr)) {
            sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC,
                    "precio");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // 2. Validar qué filtros están activos
        boolean hayCategorias = categorias != null && !categorias.isEmpty();
        boolean hayMarcas = marcas != null && !marcas.isEmpty();

        // 3. Ejecutar la consulta dinámica correspondiente
        if (hayCategorias && hayMarcas) {
            return productoRepository.findByCategoriaIdInAndMarcaIdIn(categorias, marcas, pageable);
        } else if (hayCategorias) {
            return productoRepository.findByCategoriaIdIn(categorias, pageable);
        } else if (hayMarcas) {
            return productoRepository.findByMarcaIdIn(marcas, pageable);
        } else {
            return productoRepository.findAll(pageable); // Si no hay filtros, trae todo
        }
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }
}