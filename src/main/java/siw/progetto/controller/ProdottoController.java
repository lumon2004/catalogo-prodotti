package siw.progetto.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import siw.progetto.model.*;
import siw.progetto.security.UtenteDetails;
import siw.progetto.service.ProdottoService;
import siw.progetto.service.ImageStorageService;

// Controller per la gestione dei prodotti
// Gestisce la visualizzazione, creazione, modifica ed eliminazione dei prodotti
@Controller
public class ProdottoController {

	// Iniezione di prodottoService per gestire le operazioni sui prodotti
	@Autowired
	private ProdottoService prodottoService;

	// Iniezione di imageStorageService per gestire l'upload delle immagini
	@Autowired
	private ImageStorageService imageStorageService;

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
        Utente utente = utenteDetails.getUtente();
		Prodotto prodotto = prodottoService.findById(id).orElse(null);
		if (prodotto == null) {
			return "redirect:/home";
		}

		if (utenteDetails != null) {
			model.addAttribute("utente", utenteDetails.getUtente());
		}

        model.addAttribute("isAdmin", utente.getRole() == Utente.Role.ADMIN);
		model.addAttribute("prodotto", prodotto);
		List<String> tipologie = prodottoService.findAllTipologieOrdinate();
		model.addAttribute("tipologie", tipologie);
		List<Prodotto> similiManuali = prodottoService.getManuali(prodotto);
		model.addAttribute("similiManuali", similiManuali);
		List<Prodotto> similiSuggeriti = prodottoService.getSuggeriti(prodotto);
		model.addAttribute("similiSuggeriti", similiSuggeriti);
		List<Prodotto> similiCorrelati = prodottoService.getCorrelati(prodotto);
		model.addAttribute("similiCorrelati", similiCorrelati);
		List<Commento> commenti = prodotto.getCommenti();
		model.addAttribute("commenti", commenti);
		List<String> marche = prodottoService.findAllMarcheOrdinate();
		model.addAttribute("marche", marche);
		List<Integer> anni = prodottoService.findAllAnniOrdinati();
		model.addAttribute("anni", anni);

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
	 * @param error Un parametro opzionale per indicare errori (ad esempio, nome duplicato).
	 * @return La vista del form per aggiungere un nuovo prodotto.
	 */
	@GetMapping("/prodotti/new")
	public String showFormNuovoProdotto(@RequestParam(value = "error", required = false) String error, Model model) {
		model.addAttribute("prodotto", new Prodotto());
		model.addAttribute("error", error);

		// Lista dinamica di tipologie
		List<String> tipologie = prodottoService.findAllTipologieOrdinate();
		model.addAttribute("tipologie", tipologie);
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
		// Salva immagine rinominata e convertita in .jpg
			String imageFileName = imageStorageService.salvaImmagineProdotto(file, prodotto.getNome(), prodotto.getTipologia());
			prodotto.setImagePath(imageFileName);
		} else if (defaultImage != null && !defaultImage.isBlank()) {
			// Se l’admin ha selezionato un’immagine di default
			prodotto.setImagePath("default-" + defaultImage);
		} else {
			// Fallback
			prodotto.setImagePath("default-generica.jpg");
		}

		boolean salvato = prodottoService.saveIfNotExists(prodotto);
		if (!salvato) {
			// Qui puoi decidere: rimandare al form con messaggio di errore
			// Oppure ignorare e tornare alla lista
			return "redirect:/prodotti/new?error=duplicate";
		}

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
	 * @return La vista del prodotto modificato a cui si viene reindirizzati dopo l'aggiornamento del prodotto.
	 * @throws IOException Se si verifica un errore durante il caricamento del file.
	 */
	@PostMapping("/prodotti/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String aggiornaProdotto(@ModelAttribute("prodotto") Prodotto prodotto, @RequestParam("customImage") MultipartFile file, @RequestParam(value="removeImage", required=false) String removeImage) throws IOException {

		Optional<Prodotto> existingOpt = prodottoService.findById(prodotto.getId());
		if (existingOpt.isEmpty()) {
			return "redirect:/home"; // prodotto non trovato
		}

		Prodotto existingProdotto = existingOpt.get();

		// Aggiorna i campi
		existingProdotto.setNome(prodotto.getNome());
		existingProdotto.setMarca(prodotto.getMarca());
		existingProdotto.setAnno(prodotto.getAnno());
		existingProdotto.setDescrizione(prodotto.getDescrizione());
		existingProdotto.setPrezzo(prodotto.getPrezzo());

		// Mantieni la tipologia attuale, non viene cambiata dal form
        String normalizedTipologia = existingProdotto.getTipologia();
        existingProdotto.setTipologia(normalizedTipologia);

		// Gestione immagine
		if ("true".equals(removeImage)) {
            existingProdotto.setImagePath(null); // oppure "default-generica.jpg"
        } else if (!file.isEmpty()) {
			String imagePath = imageStorageService.salvaImmagineProdotto(file, existingProdotto.getNome(), normalizedTipologia);
			existingProdotto.setImagePath(imagePath);
		}

		prodottoService.save(existingProdotto);

		return "redirect:/prodotti/" + existingProdotto.getId();
	}

	/**
	 * Elimina un prodotto.
	 * @param id L'ID del prodotto da eliminare.
	 * @return La vista della home page a cui si viene reindirizzati dopo l'eliminazione del prodotto.
	 */
	@PostMapping("/prodotti/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String eliminaProdotto(@PathVariable Long id) throws IOException {
		Optional<Prodotto> prodottoOpt = prodottoService.findById(id);
		if (prodottoOpt.isPresent()) {
			Prodotto prodotto = prodottoOpt.get();

			// Rimuovi eventuali riferimenti in prodotti simili
			for (Prodotto p : prodottoService.findAll()) {
				if (p.getProdottiSimili().contains(prodotto)) {
					p.getProdottiSimili().remove(prodotto);
					prodottoService.save(p);
				}
			}

			// Elimina immagine dal filesystem se non è default
			if (prodotto.getImagePath() != null && !prodotto.getImagePath().startsWith("default")) {
				Path path = Paths.get(System.getProperty("user.dir"), prodotto.getImagePath());
				Files.deleteIfExists(path);
			}

			prodottoService.deleteById(id);
		}
		return "redirect:/home";
	}

	@Value("${REMEMBER_ME_KEY}")
	private String rememberMeKey;

	@GetMapping("/test-key")
	public String testKey() {
		return "Key loaded: " + (rememberMeKey != null ? "✓" : "✗");
	}
}