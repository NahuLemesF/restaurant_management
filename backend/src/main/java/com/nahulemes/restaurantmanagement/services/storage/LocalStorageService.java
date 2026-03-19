package com.nahulemes.restaurantmanagement.services.storage;

import com.nahulemes.restaurantmanagement.services.interfaces.IStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Adaptador de almacenamiento local (filesystem).
 * Implementa IStorageService — si en el futuro se migra a S3,
 * sólo se crea S3StorageService sin modificar FileUploadController (OCP).
 */
@Service
public class LocalStorageService implements IStorageService {

    private final Path uploadDir;

    public LocalStorageService() {
        this.uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento.", ex);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );

        if (originalName.contains("..")) {
            throw new IllegalArgumentException("El nombre de archivo contiene una ruta inválida: " + originalName);
        }

        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        String storedName = UUID.randomUUID() + extension;

        try {
            Path target = uploadDir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo: " + originalName, ex);
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(storedName)
                .toUriString();
    }
}
