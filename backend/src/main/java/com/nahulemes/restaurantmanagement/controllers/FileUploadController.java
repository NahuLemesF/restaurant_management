package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.services.interfaces.IStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller de subida de archivos.
 * Delega 100% de la lógica de IO a IStorageService (SRP).
 * Para cambiar el destino (local → S3), solo se swappea el bean de IStorageService (OCP, DIP).
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {

    private final IStorageService storageService;

    public FileUploadController(IStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String publicUrl = storageService.store(file);
            return ResponseEntity.ok(Map.of("imageUrl", publicUrl));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo almacenar el archivo."));
        }
    }
}
