package siw.progetto.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import siw.progetto.model.Utente;

// Interfaccia per la gestione degli utenti nel database
// Estende JpaRepository per operazioni CRUD su Utente
// Utente è l'entità e Long è il tipo della chiave primaria
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    // trova un utente specifico basato sul nome utente
    Optional<Utente> findByUsername(String username);

    // trova un utente specifico basato sull'email
    Optional<Utente> findByEmail(String email);
}