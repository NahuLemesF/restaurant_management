package com.nahulemes.restaurantmanagement.repositories;

import com.nahulemes.restaurantmanagement.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDishRepository extends JpaRepository<Dish, Long> {
}
