package siw.progetto.controller;

import siw.progetto.model.Utente;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controller per la gestione degli utenti
// Gestisce la registrazione di nuovi utenti e la visualizzazione del form di registrazione
@Controller
public class UtenteController {
    // Iniezione di utenteService per gestire le operazioni sugli utenti
    @Autowired
    private UtenteService utenteService;

    /**
     * Mostra il form di registrazione per un nuovo utente.
     * Se l'utente è già autenticato, viene reindirizzato all'area personale.
     * @param model Il modello per passare i dati alla vista.
     * @param utenteDetails I dettagli dell'utente autenticato.
     * @return La vista del form di registrazione o il redirect alla home page.
     */
    @GetMapping("/utenti/new")
    public String mostraFormRegistrazione(Model model, @AuthenticationPrincipal UtenteDetails utenteDetails) {
        // Se l'utente è già autenticato, reindirizza alla home page
        if (utenteDetails != null) {
            return "redirect:/home";
        }
        model.addAttribute("utente", new Utente());
        return "nuovoUtente";
    }

    /**
     * Registra un nuovo utente.
     * @param utente L'utente da registrare.
     * @return Il redirect alla pagina di login dopo la registrazione.
     */
    @PostMapping("/utenti")
    public String registraNuovoUtente(@ModelAttribute("utente") Utente utente) {
        utente.setId(null);
        utente.setRole(Utente.Role.USER);
        utenteService.save(utente);
        return "redirect:/login";
    }
}