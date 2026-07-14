package pe.edu.utp.streetwear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carrito") 
public class CarritoController {

    @GetMapping("/checkout")
    public String procederPago() {
        return "transaccion/checkout";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(Long productoId, Integer cantidad) {
        return "redirect:/catalogo?agregado=true";
    }

    @PostMapping("/procesar")
    public String procesarCompra() {
        return "redirect:/historial?compraExitosa=true";
    }
}