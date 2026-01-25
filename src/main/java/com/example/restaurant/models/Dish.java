package com.example.restaurant.models;

import com.example.restaurant.constants.DishType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private DishType dishType = DishType.COMMON;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToMany(mappedBy = "dishes")
    private List<Order> orders = new ArrayList<>();

    public Dish(Long id, String name, String description, BigDecimal price, DishType dishType, Menu menu) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dishType = dishType;
        this.menu = menu;
    }

    public Dish() {
    }


}
