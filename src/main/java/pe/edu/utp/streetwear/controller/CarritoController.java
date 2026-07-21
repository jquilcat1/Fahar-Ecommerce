package pe.edu.utp.streetwear.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.streetwear.dto.ItemCarrito;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.model.Usuario;
import pe.edu.utp.streetwear.service.MercadoPagoService;
import pe.edu.utp.streetwear.service.ProductoService;
import pe.edu.utp.streetwear.service.TransaccionService;
import pe.edu.utp.streetwear.service.UsuarioService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final ProductoService productoService;
    private final TransaccionService transaccionService;
    private final UsuarioService usuarioService;
    private final MercadoPagoService mercadoPagoService;

    @GetMapping("/checkout")
    @SuppressWarnings("unchecked")
    public String procederPago(HttpSession session, Model model) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/catalogo";
        }
        return "transaccion/checkout";
    }

    @PostMapping("/agregar")
    @SuppressWarnings("unchecked")
    public String agregarAlCarrito(
            @RequestParam("productoId") Long productoId,
            @RequestParam(value = "talla", defaultValue = "Única") String talla,
            @RequestParam(value = "cantidad", defaultValue = "1") Integer cantidad,
            HttpSession session,
            @RequestHeader(value = "referer", required = false) String referer) {

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        boolean productoExiste = false;
        for (ItemCarrito item : carrito) {
            if (item.getProductoId().equals(productoId) && item.getTalla().equals(talla)) {
                item.setCantidad(item.getCantidad() + cantidad);
                productoExiste = true;
                break;
            }
        }

        if (!productoExiste) {
            Producto producto = productoService.buscarPorId(productoId);
            ItemCarrito nuevoItem = new ItemCarrito(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getMarca().getNombre(),
                    producto.getPrecio(),
                    talla,
                    cantidad,
                    producto.getImagenUrl());
            carrito.add(nuevoItem);
        }

        java.math.BigDecimal totalCarrito = java.math.BigDecimal.ZERO;
        for (ItemCarrito item : carrito) {
            totalCarrito = totalCarrito.add(item.getSubtotal());
        }

        session.setAttribute("carrito", carrito);
        session.setAttribute("totalCarrito", totalCarrito);

        return "redirect:" + (referer != null ? referer : "/catalogo?agregado=true");
    }

    @PostMapping("/procesar")
    @SuppressWarnings("unchecked")
    public String procesarCompra(
            @RequestParam("metodoPago") String metodoPago,
            HttpSession session,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        java.math.BigDecimal totalCarrito = (java.math.BigDecimal) session.getAttribute("totalCarrito");

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/catalogo";
        }

        try {
            Usuario usuario = usuarioService.buscarPorEmail(principal.getName());

            // Si elige tarjeta, generamos la preferencia y redirigimos a Mercado Pago
            if ("TARJETA".equals(metodoPago)) {
                // Pasamos el objeto usuario completo para incluir el DNI correctamente
                // formateado
                String preferenceUrl = mercadoPagoService.crearPreferenciaPago(carrito, usuario);
                if (preferenceUrl != null) {
                    return "redirect:" + preferenceUrl;
                } else {
                    redirectAttributes.addFlashAttribute("error", "No se pudo generar el enlace de pago.");
                    return "redirect:/carrito/checkout";
                }
            }

            // Si es transferencia manual u otro método local
            transaccionService.crearPedidoDesdeCarrito(carrito, usuario, metodoPago, totalCarrito);

            session.removeAttribute("carrito");
            session.removeAttribute("totalCarrito");

            return "redirect:/historial?compraExitosa=true";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }

    @GetMapping("/exito-mp")
    @SuppressWarnings("unchecked")
    public String pagoExitosoMercadoPago(HttpSession session, Principal principal) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        java.math.BigDecimal totalCarrito = (java.math.BigDecimal) session.getAttribute("totalCarrito");

        if (carrito != null && !carrito.isEmpty() && principal != null) {
            try {
                Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
                transaccionService.crearPedidoDesdeCarrito(carrito, usuario, "TARJETA_MERCADOPAGO", totalCarrito);

                session.removeAttribute("carrito");
                session.removeAttribute("totalCarrito");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/historial?compraExitosa=true";
    }
}