package siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import siw.progetto.model.Prodotto;
import java.util.*;

// Interfaccia per la gestione dei prodotti nel database
// Estende JpaRepository per operazioni CRUD su Prodotto
// Prodotto è l'entità e Long è il tipo della chiave primaria
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    // trova prodotti per tipologia
    List<Prodotto> findByTipologia(String tipologia);

    // trova prodotti per marca
    List<Prodotto> findByMarca(String marca);

    // trova prodotti per anno
    List<Prodotto> findByAnno(Integer anno);

    // trova prodotti che contengano una stringa nel nome
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);

    // trova tutte le tipologie distinte presenti nei prodotti
    @Query("SELECT DISTINCT p.tipologia FROM Prodotto p")
    List<String> findDistinctTipologia();

    // trova tutte le marche distinte presenti nei prodotti
    @Query("SELECT DISTINCT p.marca FROM Prodotto p ORDER BY p.marca ASC")
    List<String> findDistinctMarcheOrdinate();

    // trova tutti gli anni di produzione distinti presenti nei prodotti
    @Query("SELECT DISTINCT p.anno FROM Prodotto p ORDER BY p.anno DESC")
    List<Integer> findDistinctAnniOrdinati();

    // trova prodotti per tipologia e marca
    List<Prodotto> findByTipologiaAndMarca(String tipologia, String marca);
}