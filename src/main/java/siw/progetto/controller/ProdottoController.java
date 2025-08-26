package siw.progetto.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import siw.progetto.model.*;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;

// Controller per la gestione dei prodotti
// Gestisce la visualizzazione, creazione, modifica ed eliminazione dei prodotti
@Controller
public class ProdottoController {

    // Iniezione di prodottoService per gestire le operazioni sui prodotti
    @Autowired
    private ProdottoService prodottoService;

    /**
     * Metodo per sanificare il nome del file, rimuovendo caratteri non validi.
     * @param filename Il nome del file da sanificare.
     * @return Il nome del file sanificato.
     */
    public static String sanitizeFileName(String filename) {
        // Rimuove tutti i caratteri tranne lettere, numeri, punto e underscore
        return filename.replaceAll("[^a-zA-Z0-9.]", "");
    }

    /**
     * Mostra i dettagli di un prodotto
     * @param id l'ID del prodotto da mostrare
     * @param editCommentId l'ID del commento da modificare (opzionale)
     * @param utenteDetails i dettagli dell'utente autenticato
     * @param model il modello per la vista
     * @return il nome della vista da rendere
     */
    @GetMapping("/prodotti/{id}")
    public String mostraProdotto(@PathVariable Long id, @RequestParam(value = "editCommentId", required = false) Long editCommentId, @AuthenticationPrincipal UtenteDetails utenteDetails, Model model) {
        Prodotto prodotto = prodottoService.findById(id).orElse(null);
        if (prodotto == null) {
            return "redirect:/home";
        }

        if (utenteDetails != null) {
            model.addAttribute("utente", utenteDetails.getUtente());
        }

        List<Prodotto> prodottiSimili = prodottoService.findSimilarProducts(prodotto);
        List<Commento> commenti = prodotto.getCommenti();
        model.addAttribute("prodottiSimili", prodottiSimili);
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("commenti", commenti);

        if (editCommentId != null) {
            model.addAttribute("editCommentId", editCommentId);
        } else {
            model.addAttribute("editCommentId", null);
        }

        return "prodotto"; // thymeleaf template "prodotto.html"
    }

    /**
     * Mostra il form per aggiungere un nuovo prodotto.
     * @param model Il modello per passare i dati alla vista.
     * @return La vista del form per aggiungere un nuovo prodotto.
     */
	@GetMapping("/prodotti/new")
	public String showFormNuovoProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "nuovoProdotto";
	}

    /**
     * Crea un nuovo prodotto
     * @param prodotto il prodotto da creare
     * @param utenteDetails i dettagli dell'utente autenticato
     * @param file Il file dell'immagine del prodotto
     * @param defaultImage Il percorso dell'immagine di default se non viene caricata nessuna immagine.
     * @return il nome della vista da rendere
     * @throws IOException Se si verifica un errore durante il caricamento del file
     */
    @PostMapping("/prodotti/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String creaProdotto(@ModelAttribute("prodotto") Prodotto prodotto, @AuthenticationPrincipal UtenteDetails utenteDetails, @RequestParam("customImage") MultipartFile file, @RequestParam(value = "imagePath", required = false) String defaultImage) throws IOException {
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(originalFileName);
            String fileName = UUID.randomUUID().toString() + "_" + sanitizedFileName;

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            prodotto.setImagePath(fileName);
        } else if (defaultImage != null && !defaultImage.isBlank()) {
            prodotto.setImagePath("default-" + defaultImage); // es: default-smartphone.jpg
        } else {
            prodotto.setImagePath("default-generica.jpg"); // immagine di fallback se vuoi
        }

        prodottoService.save(prodotto);
        return "redirect:/home";
    }

    /**
     * Mostra il form per modificare un prodotto esistente.
     * @param id L'ID del prodotto da modificare.
     * @param model Il modello per passare i dati alla vista.
     * @return La vista del form per modificare il prodotto.
     */
	@GetMapping("/prodotti/edit/{id}")
	public String mostraFormModificaProdotto(@PathVariable("id") Long id, Model model) {
		Optional<Prodotto> optionalProdotto = prodottoService.findById(id);

		if (optionalProdotto.isPresent()) {
			model.addAttribute("prodotto", optionalProdotto.get());
			return "modificaProdotto";
		} else {
			return "redirect:/home"; // oppure mostra una pagina di errore
		}
	}

    /**
     * Aggiorna un prodotto esistente.
     * @param prodotto Il prodotto da aggiornare.
     * @param file Il file dell'immagine del prodotto.
     * @param removeImage Indica se l'immagine deve essere rimossa.
     * @return La vista della home page a cui si viene reindirizzati dopo l'aggiornamento del prodotto.
     * @throws IOException Se si verifica un errore durante il caricamento del file.
     */
	@PostMapping("/prodotti/update")
	public String aggiornaProdotto(@ModelAttribute("prodotto") Prodotto prodotto, @RequestParam("customImage") MultipartFile file, @RequestParam(value="removeImage", required=false) String removeImage) throws IOException {
		Optional<Prodotto> existing = prodottoService.findById(prodotto.getId());

		if (existing.isPresent()) {
			Prodotto existingProdotto = existing.get();

            if (removeImage != null) {
                // Checkbox spuntata: rimuovi immagine custom e usa default
                existingProdotto.setImagePath("default-" + existingProdotto.getImagePath().toLowerCase());
            } else if (!file.isEmpty()) {   // Mantieni l'immagine esistente se non è stata caricata una nuova
				String originalFileName = file.getOriginalFilename();
                String sanitizedFileName = sanitizeFileName(originalFileName);
                String fileName = UUID.randomUUID().toString() + "_" + sanitizedFileName;
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path filePath = uploadPath.resolve(fileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				prodotto.setImagePath(fileName);
			} else {
				prodotto.setImagePath(existingProdotto.getImagePath());
			}
		}

		prodottoService.save(prodotto);
		return "redirect:/home";
	}

    /**
     * Elimina un prodotto.
     * @param id L'ID del prodotto da eliminare.
     * @return Una risposta HTTP che indica il successo o il fallimento dell'operazione.
     */
    @DeleteMapping("/prodotti/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> eliminaProdotto(@PathVariable Long id) {
        try {
            prodottoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Errore durante eliminazione: " + e.getMessage());
            e.printStackTrace(); // importante per debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore: " + e.getMessage());
        }
    }

    @Value("${REMEMBER_ME_KEY}")
    private String rememberMeKey;

    @GetMapping("/test-key")
    public String testKey() {
        return "Key loaded: " + (rememberMeKey != null ? "✓" : "✗");
    }
}