package siw.progetto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;

@Service
public class ImageStorageService {

    public String salvaImmagineProdotto(MultipartFile file, String nomeProdotto, String tipologia) throws IOException {
        if (file.isEmpty()) return null;

        // Normalizza il nome della tipologia per usarlo come cartella
        String normalizedTipologia = tipologia.toLowerCase().trim();

        // Percorso: uploads/{tipologia}/
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", normalizedTipologia);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = nomeProdotto + ".jpg";
        Path filePath = uploadPath.resolve(fileName);

        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) throw new IOException("Il file caricato non è un'immagine valida.");

        ImageIO.write(bufferedImage, "jpg", filePath.toFile());

        // Restituisci il path relativo che puoi salvare nel DB (es: uploads/Smartphone/iPhone15.jpg)
        return "uploads/" + normalizedTipologia + "/" + fileName;
    }
}