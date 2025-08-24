package siw.progetto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Classe per generare una password hashata
// Utilizza BCryptPasswordEncoder per creare un hash della password
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin"; // cambia con la password che vuoi
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Password hashata: " + encodedPassword);
    }
}