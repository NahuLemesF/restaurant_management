package com.nahulemes.restaurantmanagement.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre del menú no puede estar vacío")
    private String name;
    @NotBlank(message = "La descripción del menú no puede estar vacía")
    private String description;

    @OneToMany(mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Dish> dishes;

    public Menu(Long id, String name, String description, List<Dish> dishes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dishes = dishes;
    }

    public Menu(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Menu() {
    }


}
