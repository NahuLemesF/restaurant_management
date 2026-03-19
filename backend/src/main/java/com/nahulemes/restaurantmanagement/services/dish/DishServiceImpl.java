package com.nahulemes.restaurantmanagement.services.dish;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.dto.dish.DishRequestDTO;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.observers.DishSubject;
import com.nahulemes.restaurantmanagement.repositories.IDishRepository;
import com.nahulemes.restaurantmanagement.repositories.IMenuRepository;
import com.nahulemes.restaurantmanagement.services.interfaces.IDishService;
import com.nahulemes.restaurantmanagement.utils.mapper.DishMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements IDishService {

    private final IDishRepository dishRepository;
    private final IMenuRepository menuRepository;
    private final DishSubject dishSubject;

    public DishServiceImpl(IDishRepository dishRepository, IMenuRepository menuRepository, DishSubject dishSubject) {
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
        this.dishSubject = dishSubject;
    }

    @Override
    @Transactional
    public Dish create(DishRequestDTO dto) {
        Menu menu = requireMenu(dto.menuId());
        Dish newDish = DishMapper.convertToEntity(dto, menu);
        Dish createdDish = dishRepository.save(newDish);
        dishSubject.notifyObservers(EventType.CREATE, createdDish);
        return createdDish;
    }

    @Override
    @Transactional(readOnly = true)
    public Dish getById(Long id) {
        return requireDish(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Dish dishToDelete = requireDish(id);
        dishRepository.delete(dishToDelete);
        dishSubject.notifyObservers(EventType.DELETE, dishToDelete);
    }

    @Override
    @Transactional
    public Dish update(Long id, DishRequestDTO dto) {
        Dish existingDish = requireDish(id);
        Menu menu = requireMenu(dto.menuId());

        existingDish.setName(dto.name());
        existingDish.setDescription(dto.description());
        existingDish.setPrice(dto.price());
        existingDish.setMenu(menu);
        // Preservar imageUrl si el DTO trae una nueva, o mantener la existente
        if (dto.imageUrl() != null && !dto.imageUrl().isBlank()) {
            existingDish.setImageUrl(dto.imageUrl());
        }

        Dish updatedDish = dishRepository.save(existingDish);
        dishSubject.notifyObservers(EventType.UPDATE, updatedDish);
        return updatedDish;
    }

    private Dish requireDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato", "id", id));
    }

    private Menu requireMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menú", "id", id));
    }
}
