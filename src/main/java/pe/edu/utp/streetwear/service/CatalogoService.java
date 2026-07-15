package pe.edu.utp.streetwear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.streetwear.dto.ProductoDTO;
import pe.edu.utp.streetwear.exception.ResourceNotFoundException;
import pe.edu.utp.streetwear.mapper.CatalogoMapper;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.repository.CategoriaRepository;
import pe.edu.utp.streetwear.repository.MarcaRepository;
import pe.edu.utp.streetwear.repository.ProductoRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
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

    // -----------------------------------------------------------
    // MOTOR DE GUARDADO DE ARCHIVOS FÍSICOS
    // -----------------------------------------------------------
    private String guardarArchivoLocal(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        try {
            // 1. Crear un nombre único usando UUID para evitar sobreescribir imágenes
            String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            
            // 2. Definir la carpeta donde se guardarán (en la raíz del proyecto)
            Path directorioUploads = Paths.get("uploads");
            if (!Files.exists(directorioUploads)) {
                Files.createDirectories(directorioUploads);
            }
            
            // 3. Copiar el archivo físico a la carpeta
            Path rutaDestino = directorioUploads.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            
            // 4. Retornar la ruta relativa que se guardará en MySQL
            return "/uploads/" + nombreUnico;
        } catch (Exception e) {
            throw new RuntimeException("Error crítico al intentar guardar la imagen: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------
    // GUARDAR PRODUCTO (AHORA SOPORTA MÚLTIPLES ARCHIVOS)
    // -----------------------------------------------------------
    public ProductoDTO guardarProducto(ProductoDTO dto) {
        
        // INTERCEPCIÓN: Procesar los archivos físicos antes de mapear a la Entidad
        if (dto.getArchivo1() != null && !dto.getArchivo1().isEmpty()) {
            dto.setImagenUrl(guardarArchivoLocal(dto.getArchivo1()));
        }
        if (dto.getArchivo2() != null && !dto.getArchivo2().isEmpty()) {
            dto.setImagenUrl2(guardarArchivoLocal(dto.getArchivo2()));
        }
        if (dto.getArchivo3() != null && !dto.getArchivo3().isEmpty()) {
            dto.setImagenUrl3(guardarArchivoLocal(dto.getArchivo3()));
        }
        if (dto.getArchivo4() != null && !dto.getArchivo4().isEmpty()) {
            dto.setImagenUrl4(guardarArchivoLocal(dto.getArchivo4()));
        }

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