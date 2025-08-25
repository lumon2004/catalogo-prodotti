package siw.progetto.service;

import siw.progetto.model.Utente;
import siw.progetto.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Servizio per la gestione degli utenti
// Fornisce metodi per operazioni CRUD sugli utenti
// Utilizza UtenteRepository per interagire con il database
@Service
public class UtenteService {

    // Inietta UtenteRepository per operazioni sul database
    @Autowired
    private UtenteRepository utenteRepository;

    // Inietta PasswordEncoder per la codifica delle password
    // Utilizzato per garantire che le password siano memorizzate in modo sicuro
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Metodo per trovare un utente per ID
    public Optional<Utente> findById(Long id) {
        return utenteRepository.findById(id);
    }

    // Metodo per salvare un utente
    // Codifica la password prima di salvarla nel database tramite PasswordEncoder
    public Utente save(Utente utente) {
        String rawPassword = utente.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        utente.setPassword(encodedPassword);
        return utenteRepository.save(utente);
    }

    // Metodo per trovare un utente per email
    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email)
            .orElseThrow(() -> new UtenteNotFoundException(email));
    }
}