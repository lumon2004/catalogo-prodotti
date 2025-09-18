package siw.progetto.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import siw.progetto.model.Prodotto;
import siw.progetto.repository.ProdottoRepository;

@Controller
@RequestMapping("/prodotti")
public class AdminProdottoController {

    private final ProdottoRepository prodottoRepository = null;

    // Mostra la pagina CRUD per i simili
    @GetMapping("/{id}/simili")
    public String editSimili(@PathVariable Long id, Model model) {
        Prodotto prodotto = prodottoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        List<Prodotto> tutti = prodottoRepository.findAll();

        model.addAttribute("prodotto", prodotto);
        model.addAttribute("tuttiProdotti", tutti);
        model.addAttribute("similiSelezionati", prodotto.getProdottiSimili());
        return "modificaSimili"; // il template che creiamo sotto
    }

    // Salva le modifiche
    @PostMapping("/{id}/simili")
    public String updateSimili(@PathVariable Long id, @RequestParam(value = "similiIds", required = false) List<Long> similiIds) {
        Prodotto prodotto = prodottoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        Set<Prodotto> nuoviSimili = new HashSet<>();
        if (similiIds != null) {
            nuoviSimili.addAll(prodottoRepository.findAllById(similiIds));
        }

        prodotto.setProdottiSimili(nuoviSimili);
        prodottoRepository.save(prodotto);

        return "redirect:/prodotti/" + id;
    }
}