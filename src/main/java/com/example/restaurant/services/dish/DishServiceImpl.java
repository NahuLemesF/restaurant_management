package com.example.restaurant.services.dish;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.dish.DishRequestDTO;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.observers.DishSubject;
import com.example.restaurant.repositories.IDishRepository;
import com.example.restaurant.repositories.IMenuRepository;
import com.example.restaurant.utils.mapper.DishMapper;
import org.springframework.stereotype.Service;

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
    public Dish create(DishRequestDTO dto) {
        Menu menu = requireMenu(dto.getMenuId());
        Dish newDish = DishMapper.convertToEntity(dto, menu);
        Dish createdDish = dishRepository.save(newDish);
        dishSubject.notifyObservers(EventType.CREATE, createdDish);
        return createdDish;
    }

    @Override
    public Dish getById(Long id) {
        return requireDish(id);
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Dish dishToDelete = requireDish(id);
        dishRepository.delete(dishToDelete);
        dishSubject.notifyObservers(EventType.DELETE, dishToDelete);
    }

    @Override
    public Dish update(Long id, DishRequestDTO dto) {
        Dish existingDish = requireDish(id);
        Menu menu = requireMenu(dto.getMenuId());

        existingDish.setName(dto.getName());
        existingDish.setDescription(dto.getDescription());
        existingDish.setPrice(dto.getPrice());
        existingDish.setMenu(menu);

        Dish updatedDish = dishRepository.save(existingDish);
        dishSubject.notifyObservers(EventType.UPDATE, updatedDish);
        return updatedDish;
    }

    private Dish requireDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato con el id " + id + " no encontrado"));
    }

    private Menu requireMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menú con el id " + id + " no encontrado"));
    }
}
