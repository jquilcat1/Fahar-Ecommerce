package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.streetwear.dto.ProductoDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.CatalogoMapper;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.repository.CategoriaRepository;
import pe.edu.utp.streetwear.repository.MarcaRepository;
import pe.edu.utp.streetwear.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final CatalogoMapper catalogoMapper;

    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll().stream()
                .map(catalogoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO buscarProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el inventario"));
        return catalogoMapper.toDTO(producto);
    }

    public ProductoDTO guardarProducto(ProductoDTO dto) {
        Producto producto = catalogoMapper.toModel(dto);

        producto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")));
        
        producto.setMarca(marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada")));

        Producto guardado = productoRepository.save(producto);
        return catalogoMapper.toDTO(guardado);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }
}