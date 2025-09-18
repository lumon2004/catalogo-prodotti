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

// Controller per la gestione delle marche
// Gestisce la visualizzazione dei prodotti per anno
@Controller
public class AnnoController {

    // Iniezione di prodottoService per gestire le operazioni sui prodotti
    @Autowired
    private ProdottoService prodottoService;

    /**
     * Mostra i prodotti di una specifica marca
     * @param marca la marca di prodotti da mostrare
     * @param utenteDetails i dettagli dell'utente autenticato
     * @param model il modello per la vista
     * @return il nome della vista da rendere
     */
    @GetMapping("/anno/{anno}")
    public String mostraProdottiPerAnno(@PathVariable Integer anno, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        Utente utente = utenteDetails.getUtente();
        List<Prodotto> prodotti = prodottoService.findByAnno(anno);
        List<String> tipologie = prodottoService.findAllTipologieOrdinate();
        List<String> marche = prodottoService.findAllMarcheOrdinate();
        List<Integer> anni = prodottoService.findAllAnniOrdinati();
        
        model.addAttribute("utente", utenteDetails != null ? utenteDetails.getUtente() : null);
        model.addAttribute("isAdmin", utente.getRole() == Utente.Role.ADMIN);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("tipologie", tipologie);
        model.addAttribute("marche", marche);
        model.addAttribute("anni", anni);
        model.addAttribute("anno", anno); // Mantieni l'originale per la vista

        return "anno";
    }
}