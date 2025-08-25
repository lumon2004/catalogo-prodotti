package siw.progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import siw.progetto.model.Commento;
import siw.progetto.model.Prodotto;
import siw.progetto.model.Utente;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.CommentoService;
import siw.progetto.service.ProdottoService;
import siw.progetto.service.UtenteService;

import java.time.LocalDateTime;

// Controller per la gestione dei commenti ai prodotti
// Permette agli utenti di aggiungere commenti ai prodotti
@Controller
public class CommentoController {

    // Iniezione di commentoService per gestire i commenti
    @Autowired
    private CommentoService commentoService;

    // Iniezione di prodottoService per gestire i prodotti
    @Autowired
    private ProdottoService prodottoService;

    // Iniezione di utenteService per gestire gli utenti
    @SuppressWarnings("unused")
    @Autowired
    private UtenteService utenteService;

    /**
     * Aggiunge un commento a un evento.
     * @param eventoId L'ID dell'evento a cui aggiungere il commento.
     * @param utenteDetails I dettagli dell'utente autenticato.
     * @param model Il modello per passare i dati alla vista.
     * @param contenuto Il contenuto del commento da aggiungere.
     * @return Il redirect alla pagina dell'evento dopo l'aggiunta del commento.
     */
    @PostMapping("/prodotti/{id}/commenta")
    public String aggiungiCommento(@PathVariable("id") Long eventoId, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model, @RequestParam("contenuto") String contenuto) {
        if (utenteDetails != null) {
            model.addAttribute("username", utenteDetails.getUsername());
            // puoi accedere a utenteDetails.getUtente() per info aggiuntive
        }

        Prodotto prodotto = prodottoService.findById(eventoId).orElse(null);
        if (utenteDetails == null) {
            return "redirect:/login";
        }
        Utente utente = utenteDetails.getUtente();

        if (prodotto != null && utente != null && contenuto != null && !contenuto.isBlank()) {
            Commento commento = new Commento();
            commento.setProdotto(prodotto);
            commento.setAutore(utente);
            commento.setTesto(contenuto);
            commento.setDataCreazione(LocalDateTime.now());

            commentoService.save(commento);
        }

        return "redirect:/prodotti/" + eventoId;
    }
}