package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.dto.client.ClientRequestDTO;
import com.nahulemes.restaurantmanagement.dto.client.ClientResponseDTO;
import com.nahulemes.restaurantmanagement.services.client.ClientService;
import com.nahulemes.restaurantmanagement.utils.mapper.ClientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDTO create(@Valid @RequestBody ClientRequestDTO dto) {
        return ClientMapper.toDto(clientService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ClientMapper.toDto(clientService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(
                clientService.getAll().stream().map(ClientMapper::toDto).toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO dto
    ) {
        return ResponseEntity.ok(ClientMapper.toDto(clientService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }
}
