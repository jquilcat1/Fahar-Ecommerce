package pe.edu.utp.streetwear.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.streetwear.dto.ItemCarrito;
import pe.edu.utp.streetwear.model.Producto;
import pe.edu.utp.streetwear.service.ProductoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final ProductoService productoService;
    

    @GetMapping("/checkout")
    public String procederPago() {
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

    @PostMapping("/eliminar")
    @SuppressWarnings("unchecked")
    public String eliminarDelCarrito(
            @RequestParam("productoId") Long productoId,
            @RequestParam("talla") String talla,
            HttpSession session,
            @RequestHeader(value = "referer", required = false) String referer) {

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        if (carrito != null) {
            // Removemos el item que coincida exactamente en ID y Talla
            carrito.removeIf(item -> item.getProductoId().equals(productoId) && item.getTalla().equals(talla));

            // Recalculamos el total
            java.math.BigDecimal totalCarrito = java.math.BigDecimal.ZERO;
            for (ItemCarrito item : carrito) {
                totalCarrito = totalCarrito.add(item.getSubtotal());
            }

            // Actualizamos la sesión
            session.setAttribute("carrito", carrito);
            session.setAttribute("totalCarrito", totalCarrito);
        }

        return "redirect:" + (referer != null ? referer : "/catalogo");
    }

        @PostMapping("/procesar")
        public String procesarCompra() {
            return "redirect:/historial?compraExitosa=true";
        }
    }