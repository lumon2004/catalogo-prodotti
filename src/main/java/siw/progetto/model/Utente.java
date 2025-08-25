package siw.progetto.model;

import java.util.*;
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

    // ENUM per i ruoli utente
    public enum Role {
        ADMIN, USER
    }

    // L'attributo role non può essere nullo
    @Enumerated(EnumType.STRING)    // Salva "ADMIN" o "USER" come testo nel database
    @Column(nullable = false)
    private Role role;

    // La relazione con i commenti
    @OneToMany(mappedBy = "autore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commento> commenti = new ArrayList<>();

    // Getters e Setters
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return this.role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public List<Commento> getCommenti() {
        return this.commenti;
    }

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