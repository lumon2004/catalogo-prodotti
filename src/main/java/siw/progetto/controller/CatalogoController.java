package siw.progetto.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import siw.progetto.model.Prodotto;
import siw.progetto.model.Utente;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;


// Controller per la visualizzazione del catalogo
@Controller
public class CatalogoController {

    // Iniezione di prodottoService per la gestione delle operazioni sui prodotti
    @Autowired
    private ProdottoService prodottoService;

    /** 
     * Metodo per mostrare l'intero catalogo di prodotti
     * @param model Il modello per la vista
     * @return il nome della vista da rendere
     */
    @GetMapping("/catalogo")
    public String mostraCatalogo(@AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        Utente utente = utenteDetails.getUtente();

        // Mappa annidata: tipologia → (marca → lista prodotti)
        Map<String, Map<String, List<Prodotto>>> prodottiPerTipologiaEMarca = prodottoService.getProdottiRaggruppatiPerTipologiaEMarca();
        model.addAttribute("catalogo", prodottiPerTipologiaEMarca);

        model.addAttribute("isAdmin", utente.getRole() == Utente.Role.ADMIN);
        List<String> tipologie = prodottoService.findAllTipologieOrdinate();
        model.addAttribute("tipologie", tipologie);
        List<String> marche = prodottoService.findAllMarcheOrdinate();
        model.addAttribute("marche", marche);
        List<Integer> anni = prodottoService.findAllAnniOrdinati();
        model.addAttribute("anni", anni);
        return "catalogo";
    }
}