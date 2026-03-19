package com.nahulemes.restaurantmanagement.models;

import com.nahulemes.restaurantmanagement.constants.ClientType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @Email(message = "Email no válido")
    private String email;

    @Enumerated(EnumType.STRING)
    private ClientType clientType = ClientType.COMMON;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Client(Long id, String name, String lastName, String email, ClientType clientType) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.clientType = clientType;
    }

    public Client() {
    }

}
