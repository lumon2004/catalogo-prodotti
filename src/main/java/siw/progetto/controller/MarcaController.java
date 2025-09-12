package siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import siw.progetto.model.Prodotto;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;

// Controller per la gestione delle marche
// Gestisce la visualizzazione dei prodotti per marca
@Controller
public class MarcaController {

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
    @GetMapping("/marca/{marca}")
    public String mostraProdottiPerMarca(@PathVariable String marca, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        if (marca.equals("hp") || marca.equals("lg") || marca.equals("tcl")) {
            marca = marca.toUpperCase();
        } else {
            marca = marca.substring(0, 1).toUpperCase() + marca.substring(1).toLowerCase();
        }
        List<Prodotto> prodotti = prodottoService.findByMarca(marca);
        List<String> tipologie = prodottoService.findAllTipologieOrdinate();
        List<String> marche = prodottoService.findAllMarcheOrdinate();
        List<Integer> anni = prodottoService.findAllAnniOrdinati();
        
        model.addAttribute("utente", utenteDetails != null ? utenteDetails.getUtente() : null);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("tipologie", tipologie);
        model.addAttribute("marche", marche);
        model.addAttribute("anni", anni);
        model.addAttribute("marca", marca); // Mantieni l'originale per la vista
        
        return "marca";
    }
}