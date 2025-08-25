package siw.progetto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller per la gestione del login
// Mostra la pagina di login quando l'utente non è autenticato
@Controller
public class LoginController {

    /**
     * Mostra la pagina di login.
     * @return La vista del login.
     */
    @GetMapping("/login")
    public String mostraLogin() {
        return "login";  // Thymeleaf cerca login.html
    }
}