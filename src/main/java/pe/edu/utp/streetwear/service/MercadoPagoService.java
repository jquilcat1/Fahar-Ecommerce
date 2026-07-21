package pe.edu.utp.streetwear.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pe.edu.utp.streetwear.dto.ItemCarrito;
import pe.edu.utp.streetwear.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    private static final String ACCESS_TOKEN = "APP_USR-3647880488579408-072106-d0e21554c2579d148a0f8dcafbc5ad99-3558099110";

    public String crearPreferenciaPago(List<ItemCarrito> carrito, Usuario usuario) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mercadopago.com/checkout/preferences";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        Map<String, Object> body = new HashMap<>();

        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (ItemCarrito item : carrito) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("title", item.getNombre() + " (Talla: " + item.getTalla() + ")");
            itemMap.put("quantity", item.getCantidad());
            itemMap.put("currency_id", "PEN");
            itemMap.put("unit_price", item.getPrecio() != null ? item.getPrecio().doubleValue() : 0.0);
            itemsList.add(itemMap);
        }
        body.put("items", itemsList);

        Map<String, String> backUrls = new HashMap<>();
        backUrls.put("success", "http://localhost:9090/carrito/exito-mp");
        backUrls.put("failure", "http://localhost:9090/carrito/checkout?error=pago_fallido");
        backUrls.put("pending", "http://localhost:9090/carrito/checkout?error=pago_pendiente");
        body.put("back_urls", backUrls);

        Map<String, Object> payer = new HashMap<>();

        // Datos básicos obligatorios
        String email = (usuario != null && usuario.getCorreo() != null) ? usuario.getCorreo() : "test_user@test.com";
        payer.put("email", email);

        String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Comprador";
        String apellidos = (usuario != null && usuario.getApellidos() != null) ? usuario.getApellidos() : "Test";
        payer.put("name", nombre);
        payer.put("surname", apellidos);

        // Teléfono con formato numérico seguro para la API
        Map<String, Object> phone = new HashMap<>();
        String telefonoStr = (usuario != null && usuario.getTelefono() != null && !usuario.getTelefono().isEmpty())
                ? usuario.getTelefono().replaceAll("\\D", "")
                : "999999999";
        phone.put("area_code", "51");
        try {
            phone.put("number", Long.parseLong(telefonoStr));
        } catch (NumberFormatException e) {
            phone.put("number", 999999999L);
        }
        payer.put("phone", phone);

        // Limpieza y envío estricto del DNI
        String dniInput = (usuario != null && usuario.getDni() != null) ? usuario.getDni() : "12345678";
        String dniLimpio = dniInput.replaceAll("\\D", "");

        if (dniLimpio.length() > 8) {
            dniLimpio = dniLimpio.substring(0, 8);
        } else if (dniLimpio.length() < 8 || dniLimpio.isEmpty()) {
            dniLimpio = "12345678";
        }

        Map<String, Object> identification = new HashMap<>();
        identification.put("type", "DNI");
        identification.put("number", dniLimpio);
        payer.put("identification", identification);

        body.put("payer", payer);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("init_point");
            }
        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            System.err.println("--- ERROR DE MERCADO PAGO --- " + errorBody);
            throw new RuntimeException("Error MP (" + e.getStatusCode() + "): " + errorBody);
        } catch (Exception e) {
            System.err.println("--- ERROR GENERAL --- " + e.getMessage());
            throw new RuntimeException("Error al conectar con Mercado Pago: " + e.getMessage());
        }

        return null;
    }
}