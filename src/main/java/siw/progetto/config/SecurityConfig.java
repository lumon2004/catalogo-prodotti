package siw.progetto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import siw.progetto.security.UtenteDetailsService;

// Configurazione della sicurezza dell'applicazione
// Gestisce l'autenticazione, autorizzazione e protezione delle risorse
// Utilizza Spring Security per configurare le regole di accesso
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Chiave per il meccanismo "remember me"
    // Utilizzata per mantenere l'utente autenticato tra le sessioni
    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    // Servizio per gestire i dettagli dell'utente
    @SuppressWarnings("unused")
    private final UtenteDetailsService utenteDetailsService;

    // Costruttore per iniettare UtenteDetailsService
    public SecurityConfig(UtenteDetailsService utenteDetailsService) {
        this.utenteDetailsService = utenteDetailsService;
    }

    // Configura le regole di sicurezza per le richieste HTTP
    // Definisce quali URL sono accessibili senza autenticazione
    // Configura la pagina di login, logout e il meccanismo "remember me"
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/logout", "/utenti/new", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key(rememberMeKey)
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 giorni
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            );
        return http.build();
    }

    // Bean per il PasswordEncoder utilizzato per l'hashing delle password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Per poter usare AuthenticationManager se serve (ad esempio in controller di login custom)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}