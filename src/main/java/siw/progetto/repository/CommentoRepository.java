package siw.progetto.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import siw.progetto.model.*;

// Interfaccia per la gestione dei commenti nel database
// Estende JpaRepository per operazioni CRUD su Commento
// Commento è l'entità e Long è il tipo della chiave primaria
public interface CommentoRepository extends JpaRepository<Commento, Long> {
    // trova commenti di un determinato utente
    List<Commento> findByAutore(Utente autore);

    // trova commenti su un determinato prodotto
    List<Commento> findByProdotto(Prodotto prodotto);
}