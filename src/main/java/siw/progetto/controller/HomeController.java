package siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import siw.progetto.model.Prodotto;
import siw.progetto.model.Utente;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;

// Controller per la gestione della home page
// Mostra i prodotti disponibili
@Controller
public class HomeController {

    // Iniezione di prodottoService per gestire i prodotti
    @Autowired private ProdottoService prodottoService;

    /**
     * Mostra la home page con i prodotti disponibili.
     * @param utenteDetails I dettagli dell'utente autenticato.
     * @param model Il modello per passare i dati alla vista.
     * @return La vista della home page.
     */
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        Utente utente = utenteDetails.getUtente();

        List<Prodotto> tutti = prodottoService.findAll();
        model.addAttribute("utente", utente);
        model.addAttribute("tuttiProdotti", tutti);
        model.addAttribute("isAdmin", utente.getRole() == Utente.Role.ADMIN);
        return "home";
    }

    /**
     * Reindirizza alla pagina di login se l'utente non è autenticato.
     * @return Il redirect alla pagina di login.
     */
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}