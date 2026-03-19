package com.nahulemes.restaurantmanagement.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

/**
 * Puerto de entrada (inbound port) para almacenamiento de archivos.
 * Permite cambiar la implementación (local, S3, GCS) sin modificar el controlador (OCP).
 */
public interface IStorageService {
    /**
     * Persiste el archivo y retorna la URL pública accesible desde el frontend.
     * @param file Archivo multipart a almacenar
     * @return URL pública relativa del recurso almacenado
     */
    String store(MultipartFile file);
}
