package siw.progetto.model;

import jakarta.persistence.*;

@Entity
public class Utente {
    /* Attributi entità Utente */

    // Attributo identificativo dell'utente
    // Utilizzo di una sequenza per la generazione automatica degli ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utente_seq")
    @SequenceGenerator(name = "utente_seq", sequenceName = "utente_id_seq", allocationSize = 1)
    private Long id;

    // L'attributo username non può essere nullo e deve essere unico
    @Column(nullable = false, unique = true)
    private String username;

    // L'attributo email non può essere nullo e deve essere unico
    @Column(nullable = false, unique = true)
    private String email;

    // L'attributo password non può essere nullo
    @Column(nullable = false)
    private String password;

    // L'attributo role non può essere nullo
    @Column(nullable = false)
    private String role;

    // Getters e Setters

    // metodi equals e hashCode
    @Override
    public boolean equals(Object o) {
        Utente that = (Utente) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() * 31;
    }
}