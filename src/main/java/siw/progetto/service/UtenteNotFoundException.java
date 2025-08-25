package siw.progetto.service;

// Eccezione personalizzata per gestire il caso in cui un utente non venga trovato
// Estende RuntimeException per essere utilizzata in contesti di runtime
public class UtenteNotFoundException extends RuntimeException {
    public UtenteNotFoundException(String email) {
        super("Utente con email " + email + " non trovato.");
    }
}