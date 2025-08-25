package siw.progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import siw.progetto.service.ProdottoService;

@Controller
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping("/prodotti")
    public String getListProdotti(Model model) {
        model.addAttribute("prodotti", prodottoService.findAll());
        return "prodotti"; // thymeleaf template "prodotti.html"
    }

    @Value("${REMEMBER_ME_KEY}")
    private String rememberMeKey;

    @GetMapping("/test-key")
    public String testKey() {
        return "Key loaded: " + (rememberMeKey != null ? "✓" : "✗");
    }
}