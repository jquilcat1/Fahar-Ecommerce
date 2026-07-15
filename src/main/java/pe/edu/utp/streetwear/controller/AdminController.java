package pe.edu.utp.streetwear.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.streetwear.dto.ProductoDTO;
import pe.edu.utp.streetwear.repository.CategoriaRepository;
import pe.edu.utp.streetwear.repository.MarcaRepository;
import pe.edu.utp.streetwear.repository.MensajeContactoRepository;
import pe.edu.utp.streetwear.service.CatalogoService;

@Controller
@RequestMapping("/admin") 
@RequiredArgsConstructor
public class AdminController {

    private final CatalogoService catalogoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final MensajeContactoRepository mensajeContactoRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/productos")
    public String gestionarProductos(Model model) {
        model.addAttribute("productos", catalogoService.listarProductos());
        return "admin/productos-lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("productoDTO", new ProductoDTO());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
        return "admin/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/producto-form";
        }
        catalogoService.guardarProducto(productoDTO);
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        catalogoService.eliminarProducto(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        ProductoDTO productoDTO = catalogoService.buscarProductoPorId(id); 
        model.addAttribute("productoDTO", productoDTO);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("marcas", marcaRepository.findAll());
        return "admin/producto-form";
    }

    @GetMapping("/mensajes")
    public String bandejaMensajes(Model model) {
        model.addAttribute("mensajesPendientes", mensajeContactoRepository.findByEstado("PENDIENTE"));
        model.addAttribute("reclamos", mensajeContactoRepository.findByAsunto("reclamo"));
        return "admin/bandeja-mensajes"; // Vista que crearemos después para el panel
    }
}