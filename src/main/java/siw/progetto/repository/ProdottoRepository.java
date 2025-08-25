package siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import siw.progetto.model.Prodotto;
import java.util.*;

// Interfaccia per la gestione dei prodotti nel database
// Estende JpaRepository per operazioni CRUD su Prodotto
// Prodotto è l'entità e Long è il tipo della chiave primaria
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    // trova prodotti per tipologia
    List<Prodotto> findByTipologia(String tipologia);

    // trova prodotti che contengano una stringa nel nome
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);
}