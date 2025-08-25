package siw.progetto.service;

import siw.progetto.model.Commento;
import siw.progetto.repository.CommentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Servizio per la gestione dei commenti
// Fornisce metodi per operazioni CRUD sui commenti
// Utilizza CommentoRepository per interagire con il database
@Service
public class CommentoService {

    // Inietta CommentoRepository per operazioni sul database
    @Autowired
    private CommentoRepository commentoRepository;

    // Metodo per trovare tutti i commenti
    public List<Commento> findAll() {
        return commentoRepository.findAll();
    }

    // Metodo per trovare un commento per ID
    public Optional<Commento> findById(Long id) {
        return commentoRepository.findById(id);
    }

    // Metodo per salvare un commento
    public Commento save(Commento commento) {
        return commentoRepository.save(commento);
    }

    // Metodo per eliminare un commento per ID
    public void deleteById(Long id) {
        commentoRepository.deleteById(id);
    }
}