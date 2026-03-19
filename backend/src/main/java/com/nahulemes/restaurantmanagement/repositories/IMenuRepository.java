package com.nahulemes.restaurantmanagement.repositories;

import com.nahulemes.restaurantmanagement.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMenuRepository extends JpaRepository<Menu, Long> {
}
