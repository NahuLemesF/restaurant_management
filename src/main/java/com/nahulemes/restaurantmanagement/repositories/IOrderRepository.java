package com.nahulemes.restaurantmanagement.repositories;

import com.nahulemes.restaurantmanagement.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    Long countByDishesId(Long dishId);

    Long countByClientId(Long clientId);
}

