package siw.progetto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Commento {
    /* Attributi entità Commento */

    // Attributo identificativo del commento
    // Utilizzo di una sequenza per la generazione automatica degli ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commento_seq")
    @SequenceGenerator(name = "commento_seq", sequenceName = "commento_id_seq", allocationSize = 1)
    private Long id;

    // L'attributo testo non può essere nullo e deve avere una lunghezza massima di 1000 caratteri
    @Column(nullable = false, length = 1000)
    private String testo;

    // L'attributo dataCreazione non può essere nullo
    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    // La relazione con Utente (autore del commento)
    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente autore;

    // Relazione con Prodotto (prodotto commentato)
    @ManyToOne(optional = false)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    /* Getters e Setters */
    public Long getId() { 
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTesto() {
        return this.testo;
    }
    public void setTesto(String testo) {
        this.testo = testo;
    }
    public LocalDateTime getDataCreazione() {
        return this.dataCreazione;
    }
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
    public Utente getAutore() {
        return this.autore;
    }
    public void setAutore(Utente autore) {
        this.autore = autore;
    }
    public Prodotto getProdotto() {
        return this.prodotto;
    }
    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    /* equals e hashCode */
    @Override
    public boolean equals(Object o) {
        Commento that = (Commento) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id) * 31;
    }
}