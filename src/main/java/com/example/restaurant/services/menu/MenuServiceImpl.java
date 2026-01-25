package com.example.restaurant.services.menu;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.menu.MenuRequestDTO;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Menu;
import com.example.restaurant.observers.MenuSubject;
import com.example.restaurant.repositories.IDishRepository;
import com.example.restaurant.repositories.IMenuRepository;
import com.example.restaurant.utils.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final IMenuRepository menuRepository;
    private final IDishRepository dishRepository;
    private final MenuSubject menuSubject;

    public MenuServiceImpl(IMenuRepository menuRepository, IDishRepository dishRepository, MenuSubject menuSubject) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.menuSubject = menuSubject;
    }

    @Override
    public Menu create(MenuRequestDTO dto) {
        List<Dish> dishes = getDishesFromDto(dto);
        Menu newMenu = MenuMapper.convertToEntity(dto, dishes);
        Menu createdMenu = menuRepository.save(newMenu);
        menuSubject.notifyObservers(EventType.CREATE, createdMenu);
        return createdMenu;
    }

    @Override
    public Menu getById(Long id) {
        return requireMenu(id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Menu menuToDelete = requireMenu(id);
        menuRepository.delete(menuToDelete);
        menuSubject.notifyObservers(EventType.DELETE, menuToDelete);
    }

    @Override
    public Menu update(Long id, MenuRequestDTO dto) {
        Menu existingMenu = requireMenu(id);
        List<Dish> dishes = getDishesFromDto(dto);

        existingMenu.setName(dto.getName());
        existingMenu.setDescription(dto.getDescription());
        existingMenu.setDishes(dishes);

        Menu updatedMenu = menuRepository.save(existingMenu);
        menuSubject.notifyObservers(EventType.UPDATE, updatedMenu);
        return updatedMenu;
    }

    private Menu requireMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menú", "id", id));
    }

    private Dish requireDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plato", "id", id));
    }

    private List<Dish> getDishesFromDto(MenuRequestDTO dto) {
        return Optional.ofNullable(dto.getDishIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::requireDish)
                .collect(Collectors.toList());
    }
}
