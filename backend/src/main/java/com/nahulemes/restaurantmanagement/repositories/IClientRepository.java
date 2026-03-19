package com.nahulemes.restaurantmanagement.repositories;

import com.nahulemes.restaurantmanagement.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {
}
