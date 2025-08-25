package siw.progetto.security;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import siw.progetto.model.Utente;

import java.util.Collection;

// Classe che implementa UserDetails per fornire i dettagli dell'utente autenticato
public class UtenteDetails implements UserDetails {

    private final Utente utente;

    // Costruttore che accetta un oggetto Utente
    public UtenteDetails(Utente utente) {
        this.utente = utente;
    }

    // Implementazione dei metodi di UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + utente.getRole().name()));
    }
    @Override
    public String getPassword() {
        return utente.getPassword();
    }
    @Override
    public String getUsername() {
        return utente.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Utente getUtente() {
        return utente;
    }
}