package pe.edu.utp.streetwear.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.utp.streetwear.model.MensajeContacto;
import pe.edu.utp.streetwear.repository.CategoriaRepository;
import pe.edu.utp.streetwear.repository.MarcaRepository;
import pe.edu.utp.streetwear.repository.MensajeContactoRepository;
import pe.edu.utp.streetwear.repository.ReclamacionRepository;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.service.CatalogoService;
import pe.edu.utp.streetwear.service.InteraccionService;
import pe.edu.utp.streetwear.service.ProductoService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CatalogoService catalogoService;
    private final InteraccionService interaccionService;
    private final ProductoService productoService;
    private final MensajeContactoRepository mensajeRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ReclamacionRepository reclamacionRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        return "public/index";
    }

    @GetMapping("/catalogo")
    public String verCatalogo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) List<Integer> categorias,
            @RequestParam(required = false) List<Integer> marcas,
            @RequestParam(defaultValue = "3") String sort,
            Model model) {

        int prendasPorPagina = 9;

        Page<Producto> productosPage = productoService.listarProductosFiltrados(page, prendasPorPagina, categorias,
                marcas, sort);

        model.addAttribute("productosPage", productosPage);

        model.addAttribute("categoriasSeleccionadas", categorias);
        model.addAttribute("marcasSeleccionadas", marcas);
        model.addAttribute("sortSeleccionado", sort);

        model.addAttribute("listaCategorias", categoriaRepository.findAll());
        model.addAttribute("listaMarcas", marcaRepository.findAll());

        return "public/catalogo";
    }

    @GetMapping("/producto/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", catalogoService.buscarProductoPorId(id));
        model.addAttribute("resenas", interaccionService.listarResenasPorProducto(id));
        return "public/producto-detalle";
    }

    @GetMapping("/nosotros")
    public String sobreNosotros() {
        return "public/nosotros";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "public/contacto";
    }

    @PostMapping("/contacto/enviar")
    public String enviarMensajeContacto(
            @RequestParam("nombre") String nombre,
            @RequestParam("correo") String correo,
            @RequestParam("asunto") String asunto,
            @RequestParam("mensaje") String mensaje,
            RedirectAttributes redirectAttributes) {

        MensajeContacto nuevoMensaje = new MensajeContacto();
        nuevoMensaje.setNombre(nombre);
        nuevoMensaje.setCorreo(correo);
        nuevoMensaje.setAsunto(asunto);
        nuevoMensaje.setMensaje(mensaje);

        mensajeRepository.save(nuevoMensaje);

        redirectAttributes.addFlashAttribute("exito",
                "Tu mensaje ha sido enviado correctamente. Te contactaremos pronto.");

        return "redirect:/contacto";
    }

    @GetMapping("/terminos")
    public String mostrarTerminos() {
        return "terminos";
    }

    @GetMapping("/politicas")
    public String mostrarPoliticas() {
        return "politicas";
    }

    @GetMapping("/reclamaciones")
    public String mostrarLibroReclamaciones() {
        return "libro-reclamaciones";
    }

    @PostMapping("/reclamaciones/guardar")
    public String guardarReclamo(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("numeroDocumento") String numeroDocumento,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam(value = "tipoBien", required = false) String tipoBien,
            @RequestParam(value = "montoReclamado", required = false) Double montoReclamado,
            @RequestParam(value = "detalleBien", required = false) String detalleBien,
            @RequestParam("tipoReclamo") String tipoReclamo,
            @RequestParam("detalleReclamo") String detalleReclamo,
            @RequestParam("pedidoConsumidor") String pedidoConsumidor,
            RedirectAttributes redirectAttributes) {

        pe.edu.utp.streetwear.model.Reclamacion nuevoReclamo = new pe.edu.utp.streetwear.model.Reclamacion();

        nuevoReclamo.setNombres(nombres);
        nuevoReclamo.setApellidos(apellidos);
        nuevoReclamo.setTipoDocumento(tipoDocumento);
        nuevoReclamo.setNumeroDocumento(numeroDocumento);
        nuevoReclamo.setDireccion(direccion);
        nuevoReclamo.setTelefono(telefono);
        nuevoReclamo.setCorreo(correo);
        nuevoReclamo.setTipoBien(tipoBien);
        nuevoReclamo.setMontoReclamado(montoReclamado);
        nuevoReclamo.setDetalleBien(detalleBien);
        nuevoReclamo.setTipoReclamo(tipoReclamo);
        nuevoReclamo.setDetalleReclamo(detalleReclamo);
        nuevoReclamo.setPedidoConsumidor(pedidoConsumidor);

        reclamacionRepository.save(nuevoReclamo);

        redirectAttributes.addFlashAttribute("exito",
                "Su " + tipoReclamo.toLowerCase() + " ha sido registrado exitosamente con el número de seguimiento #"
                        + nuevoReclamo.getId()
                        + ". Nos comunicaremos con usted en un plazo máximo de 15 días hábiles al correo: " + correo);

        return "redirect:/reclamaciones";
    }
}