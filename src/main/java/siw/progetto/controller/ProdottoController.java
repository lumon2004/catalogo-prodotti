package siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import siw.progetto.model.*;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;

@Controller
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping("/prodotti/{id}")
    public String mostraProdotto(@PathVariable Long id, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        if (utenteDetails != null) {
            model.addAttribute("utente", utenteDetails.getUtente());
        }
        Prodotto prodotto = prodottoService.findById(id).orElse(null);
        if (utenteDetails == null) {
            return "redirect:/login";
        }
        List<Prodotto> prodottiSimili = prodottoService.findSimilarProducts(prodotto);
        List<Commento> commenti = prodotto.getCommenti();
        model.addAttribute("prodottiSimili", prodottiSimili);
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("commenti", commenti);
        return "prodotto"; // thymeleaf template "prodotto.html"
    }

    @Value("${REMEMBER_ME_KEY}")
    private String rememberMeKey;

    @GetMapping("/test-key")
    public String testKey() {
        return "Key loaded: " + (rememberMeKey != null ? "✓" : "✗");
    }
}