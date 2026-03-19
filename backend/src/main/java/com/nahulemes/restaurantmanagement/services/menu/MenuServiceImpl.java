package com.nahulemes.restaurantmanagement.services.menu;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.dto.menu.MenuRequestDTO;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.observers.MenuSubject;
import com.nahulemes.restaurantmanagement.repositories.IDishRepository;
import com.nahulemes.restaurantmanagement.repositories.IMenuRepository;
import com.nahulemes.restaurantmanagement.utils.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Menu create(MenuRequestDTO dto) {
        List<Dish> dishes = getDishesFromDto(dto);
        Menu newMenu = MenuMapper.convertToEntity(dto, dishes);
        Menu createdMenu = menuRepository.save(newMenu);
        menuSubject.notifyObservers(EventType.CREATE, createdMenu);
        return createdMenu;
    }

    @Override
    @Transactional(readOnly = true)
    public Menu getById(Long id) {
        return requireMenu(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Menu menuToDelete = requireMenu(id);
        menuRepository.delete(menuToDelete);
        menuSubject.notifyObservers(EventType.DELETE, menuToDelete);
    }

    @Override
    @Transactional
    public Menu update(Long id, MenuRequestDTO dto) {
        Menu existingMenu = requireMenu(id);
        List<Dish> dishes = getDishesFromDto(dto);

        existingMenu.setName(dto.name());
        existingMenu.setDescription(dto.description());
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
        return Optional.ofNullable(dto.dishIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::requireDish)
                .collect(Collectors.toList());
    }
}
