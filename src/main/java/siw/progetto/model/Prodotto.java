package siw.progetto.model;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Prodotto {
    /* Attributi entità Prodotto */

    // Attributo identificativo del prodotto
    // Utilizzo di una sequenza per la generazione automatica degli ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodotto_id_seq")
    @SequenceGenerator(name = "prodotto_id_seq", sequenceName = "prodotto_id_seq", allocationSize = 1)
    private Long id;

    // L'attributo nome non può essere nullo
    @Column(nullable = false)
    private String nome;

    // L'attributo prezzo non può essere nullo
    @Column(nullable = false)
    private Double prezzo;

    // L'attributo descrizione può essere nullo
    @Column(nullable = true)
    private String descrizione;

    // L'attributo tipologia non può essere nullo
    @Column(nullable = false)
    private String tipologia;

    // La relazione many-to-many auto-referenziale per "prodotti simili"
    // La tabella di join avrà due colonne: prodotto_id e simile_id
    @ManyToMany
    @JoinTable(
        name = "prodotti_simili",
        joinColumns = @JoinColumn(name = "prodotto_id"),
        inverseJoinColumns = @JoinColumn(name = "simile_id")
    )
    private Set<Prodotto> prodottiSimili = new HashSet<>();


    // La relazione con i commenti
    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commento> commenti = new ArrayList<>();

    // Getters e Setters
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Double getPrezzo() {
        return this.prezzo;
    }
    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }
    public String getDescrizione() {
        return this.descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public String getTipologia() {
        return this.tipologia;
    }
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    public Set<Prodotto> getProdottiSimili() {
        return this.prodottiSimili;
    }
    public void setProdottiSimili(Set<Prodotto> prodottiSimili) {
        this.prodottiSimili = prodottiSimili;
    }
    public List<Commento> getCommenti() {
        return this.commenti;
    }
    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    // metodi equals e hashCode
    @Override
    public boolean equals(Object o) {
        Prodotto that = (Prodotto) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() * 31;
    }
}