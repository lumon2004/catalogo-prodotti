package siw.progetto.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

    public String getTempoTrascorso() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dataCreazione, now);

        long secondiTotali = duration.getSeconds();

        if (secondiTotali < 60) {
            return "Adesso";
        }

        long minuti = secondiTotali / 60;
        if (minuti < 60) {
            return minuti == 1 ? "1 minuto fa" : minuti + " minuti fa";
        }

        long ore = minuti / 60;
        if (ore < 24) {
            return ore == 1 ? "1 ora fa" : ore + " ore fa";
        }

        // calcolo giorni, mesi e anni con Period per maggiore precisione
        LocalDate dataCreazioneDate = dataCreazione.toLocalDate();
        LocalDate nowDate = now.toLocalDate();
        Period period = Period.between(dataCreazioneDate, nowDate);

        if (period.getYears() > 0) {
            return period.getYears() == 1 ? "1 anno fa" : period.getYears() + " anni fa";
        }

        if (period.getMonths() > 0) {
            return period.getMonths() == 1 ? "1 mese fa" : period.getMonths() + " mesi fa";
        }

        if (period.getDays() > 0) {
            return period.getDays() == 1 ? "1 giorno fa" : period.getDays() + " giorni fa";
        }

        // fallback: meno di un giorno ma più di 23 ore
        return "1 giorno fa";
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