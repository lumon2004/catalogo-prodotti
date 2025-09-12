package siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siw.progetto.model.Prodotto;
import siw.progetto.repository.ProdottoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Prodotto> findAll() {
        return prodottoRepository.findAll();
    }

    public Optional<Prodotto> findById(Long id) {
        return prodottoRepository.findById(id);
    }

    public List<Prodotto> findByTipologia(String tipologia) {
        return prodottoRepository.findByTipologia(tipologia);
    }

    public List<Prodotto> findByMarca(String marca) {
        return prodottoRepository.findByMarca(marca);
    }

    public List<Prodotto> findByAnno(Integer anno) {
        return prodottoRepository.findByAnno(anno);
        
    }

    public Prodotto save(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public void deleteById(Long id) {
        prodottoRepository.deleteById(id);
    }

    public List<Prodotto> findSimilarProducts(Prodotto prodotto) {
    // Recupera tutti i prodotti della stessa tipologia e marca
    List<Prodotto> candidates = prodottoRepository.findByTipologiaAndMarca(
        prodotto.getTipologia(), prodotto.getMarca()
    );

    // Filtra quelli entro 200€ di differenza rispetto al prezzo del prodotto corrente
    double prezzoMin = prodotto.getPrezzo() - 200;
    double prezzoMax = prodotto.getPrezzo() + 200;

    return candidates.stream()
                     .filter(p -> !p.getId().equals(prodotto.getId())) // esclude il prodotto stesso
                     .filter(p -> p.getPrezzo() >= prezzoMin && p.getPrezzo() <= prezzoMax)
                     .toList();
}


    public List<String> findAllTipologie() {
        return prodottoRepository.findDistinctTipologia();
    }

    private final List<String> ordinePersonalizzato = Arrays.asList(
        "SMARTPHONE", "COMPUTER", "TABLET", "SMARTWATCH", "MONITOR", "TV", "CONSOLE", "AUDIO"
    );
    
    public List<String> findAllTipologieOrdinate() {
        List<String> tipologie = findAllTipologie();
        
        tipologie.sort((a, b) -> {
            int indexA = ordinePersonalizzato.indexOf(a.toUpperCase());
            int indexB = ordinePersonalizzato.indexOf(b.toUpperCase());
            if (indexA == -1) indexA = Integer.MAX_VALUE;
            if (indexB == -1) indexB = Integer.MAX_VALUE;
            return Integer.compare(indexA, indexB);
        });
        
        return tipologie;
    }

    public List<String> findAllMarcheOrdinate() {
        List<String> marche = prodottoRepository.findDistinctMarcheOrdinate();
        return marche;
    }

    public List<Integer> findAllAnniOrdinati() {
        List<Integer> anni = prodottoRepository.findDistinctAnniOrdinati();
        return anni;
    }
}