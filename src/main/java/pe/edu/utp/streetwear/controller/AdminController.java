package pe.edu.utp.streetwear.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.streetwear.dto.ProductoDTO;
import pe.edu.utp.streetwear.model.Reclamacion;
import pe.edu.utp.streetwear.repository.CategoriaRepository;
import pe.edu.utp.streetwear.repository.MarcaRepository;
import pe.edu.utp.streetwear.repository.MensajeContactoRepository;
import pe.edu.utp.streetwear.repository.ReclamacionRepository;
import pe.edu.utp.streetwear.service.CatalogoService;
import pe.edu.utp.streetwear.service.UsuarioService;
import org.springframework.data.domain.Sort;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CatalogoService catalogoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final MensajeContactoRepository mensajeContactoRepository;
    private final UsuarioService usuarioService;
    private final ReclamacionRepository reclamacionRepository;

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

        List<Reclamacion> listaReclamos = reclamacionRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro"));
        model.addAttribute("reclamaciones", listaReclamos);

        return "admin/bandeja-mensajes";
    }

    @GetMapping("/ventas")
    public String verVentas(Model model) {
        // model.addAttribute("ventas", ventaService.obtenerTodas());
        return "admin/ventas";
    }

    @GetMapping("/clientes")
    public String verClientes(Model model) {
        model.addAttribute("clientes", usuarioService.listarTodos());
        return "admin/clientes";
    }
}