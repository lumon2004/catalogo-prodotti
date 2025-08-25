package siw.progetto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import siw.progetto.model.Utente;
import siw.progetto.repository.UtenteRepository;

import java.util.Optional;

// Servizio per caricare i dettagli dell'utente per l'autenticazione
// Implementa UserDetailsService per fornire i dettagli dell'utente basati su username
// Utilizza UtenteRepository per recuperare l'utente dal database
@Service
public class UtenteDetailsService implements UserDetailsService {

    // Inietta il repository degli utenti per accedere ai dati degli utenti
    @Autowired
    private UtenteRepository utenteRepository;

    // Metodo per caricare i dettagli dell'utente basati sul nome utente
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utenteOpt = utenteRepository.findByUsername(username);
        if (utenteOpt.isEmpty()) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }
        return new UtenteDetails(utenteOpt.get());
    }
}