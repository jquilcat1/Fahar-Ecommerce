package pe.edu.utp.streetwear.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404"; 
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("error", "Ocurrió un error inesperado en FAHAR. Inténtalo de nuevo más tarde.");
        ex.printStackTrace(); 
        return "error/500"; 
    }
}