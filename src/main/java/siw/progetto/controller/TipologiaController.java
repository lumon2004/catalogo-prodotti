package siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import siw.progetto.model.Prodotto;
import siw.progetto.model.Utente;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;

// Controller per la gestione delle tipologie
// Gestisce la visualizzazione dei prodotti per tipologia
@Controller
public class TipologiaController {

    // Iniezione di prodottoService per gestire le operazioni sui prodotti
    @Autowired
    private ProdottoService prodottoService;

    /**
     * Mostra i prodotti di una specifica tipologia
     * @param tipologia la tipologia di prodotti da mostrare
     * @param utenteDetails i dettagli dell'utente autenticato
     * @param model il modello per la vista
     * @return il nome della vista da rendere
     */
    @GetMapping("/tipologia/{tipologia}")
    public String mostraProdottiPerTipologia(@PathVariable String tipologia, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        Utente utente = utenteDetails.getUtente();
        // Converti in uppercase per la ricerca nel database
        String tipologiaUppercase = tipologia.toUpperCase();
        List<Prodotto> prodotti = prodottoService.findByTipologia(tipologiaUppercase);
        List<String> tipologie = prodottoService.findAllTipologieOrdinate();
        List<String> marche = prodottoService.findAllMarcheOrdinate();
        List<Integer> anni = prodottoService.findAllAnniOrdinati();

        model.addAttribute("utente", utente);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("tipologie", tipologie);
        model.addAttribute("marche", marche);
        model.addAttribute("anni", anni);
        model.addAttribute("isAdmin", utente.getRole() == Utente.Role.ADMIN);
        model.addAttribute("tipologia", tipologia); // Mantieni l'originale per la vista
        
        return "tipologia";
    }
}