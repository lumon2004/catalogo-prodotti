package siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siw.progetto.model.Prodotto;
import siw.progetto.repository.ProdottoRepository;

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

    public Prodotto save(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public void deleteById(Long id) {
        prodottoRepository.deleteById(id);
    }

    public List<Prodotto> findSimilarProducts(Prodotto prodotto) {
        return prodottoRepository.findByTipologia(prodotto.getTipologia());
    }
}