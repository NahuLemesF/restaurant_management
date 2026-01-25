package com.example.restaurant.services.dish;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.observers.DishSubject;
import com.example.restaurant.repositories.IDishRepository;
import com.example.restaurant.repositories.IMenuRepository;
import com.example.restaurant.utils.mapper.DishMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

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
