package siw.progetto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configurazione delle risorse statiche dell'applicazione
// Gestisce la mappatura delle risorse statiche come immagini, CSS e JavaScript
// Utilizza WebMvcConfigurer per personalizzare la configurazione di Spring MVC
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configura i gestori di risorse per servire file statici
    // Aggiunge un gestore per le risorse nella cartella "uploads"
    // Le risorse saranno accessibili tramite l'URL "/uploads/**"
    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/"); // usa path assoluto se necessario
    }
}