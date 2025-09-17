package siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siw.progetto.model.Prodotto;
import siw.progetto.repository.ProdottoRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    // Recupera i simili definiti manualmente
    public List<Prodotto> getManuali(Prodotto prodotto) {
        Set<Prodotto> manuali = prodotto.getProdottiSimili();
        return manuali.stream()
            .sorted((p1, p2) -> Double.compare(p2.getPrezzo(), p1.getPrezzo())) // ordine decrescente
            .toList();
    }

    // Simili suggeriti
    // Recupera tutti i prodotti della stessa tipologia, della stessa marca e con una variazione di prezzo di 200€
    public List<Prodotto> getSuggeriti(Prodotto prodotto) {
        List<Prodotto> candidates = prodottoRepository.findByTipologiaAndMarca(prodotto.getTipologia(), prodotto.getMarca());

        // Filtra quelli entro 200€ di differenza rispetto al prezzo del prodotto corrente
        double prezzoMin = prodotto.getPrezzo() - 200;
        double prezzoMax = prodotto.getPrezzo() + 200;

        return candidates.stream()
            .filter(p -> !p.getId().equals(prodotto.getId())) // esclude il prodotto stesso
            .filter(p -> p.getPrezzo() >= prezzoMin && p.getPrezzo() <= prezzoMax)
            .sorted((p1, p2) -> Double.compare(p2.getPrezzo(), p1.getPrezzo())) // ordine decrescente
            .toList();
    }

    // Simili correlati
    // Recupera tutti i prodotti della stessa tipologia e con una variazione di prezzo di 200€
    public List<Prodotto> getCorrelati(Prodotto prodotto) {
        List<Prodotto> candidates = prodottoRepository.findByTipologia(prodotto.getTipologia());

        // Filtra per prezzo
        double prezzoMin = prodotto.getPrezzo() - 200;
        double prezzoMax = prodotto.getPrezzo() + 200;

        return candidates.stream()
            .filter(p -> !p.getId().equals(prodotto.getId())) // esclude il prodotto stesso
            .filter(p -> p.getPrezzo() >= prezzoMin && p.getPrezzo() <= prezzoMax)
            .sorted((p1, p2) -> Double.compare(p2.getPrezzo(), p1.getPrezzo())) // ordine decrescente
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

    public Map<String, Map<String, List<Prodotto>>> getProdottiRaggruppatiPerTipologiaEMarca() {
        List<Prodotto> tutti = prodottoRepository.findAll();

        Map<String, Map<String, List<Prodotto>>> raggruppati = new LinkedHashMap<>();

        for (String tip : ordinePersonalizzato) {
            // filtro per tipologia
            List<Prodotto> prodottiTipologia = tutti.stream()
                    .filter(p -> p.getTipologia().equalsIgnoreCase(tip))
                    .toList();

            if (!prodottiTipologia.isEmpty()) {
                // raggruppo per marca mantenendo ordine alfabetico
                Map<String, List<Prodotto>> perMarca = prodottiTipologia.stream()
                        .collect(Collectors.groupingBy(
                                Prodotto::getMarca,
                                TreeMap::new, // marche in ordine alfabetico
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                    .sorted(Comparator.comparing(Prodotto::getId))
                                                    .toList()
                                )
                        ));

                raggruppati.put(tip, perMarca);
            }
        }

        return raggruppati;
    }

    public boolean saveIfNotExists(Prodotto prodotto) {
        if (prodottoRepository.existsByNome(prodotto.getNome())) {
            return false; // già esiste → non salvare
        }
        prodottoRepository.save(prodotto);
        return true; // nuovo prodotto inserito
    }
}