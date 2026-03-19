package com.nahulemes.restaurantmanagement.services.order;

import com.nahulemes.restaurantmanagement.constants.EventType;
import com.nahulemes.restaurantmanagement.dto.order.OrderRequestDTO;
import com.nahulemes.restaurantmanagement.exception.ResourceNotFoundException;
import com.nahulemes.restaurantmanagement.handlers.OrderProcessingChain;
import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.observers.OrderSubject;
import com.nahulemes.restaurantmanagement.repositories.IOrderRepository;
import com.nahulemes.restaurantmanagement.services.client.ClientService;
import com.nahulemes.restaurantmanagement.services.dish.DishService;
import com.nahulemes.restaurantmanagement.utils.OrderPriceCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final IOrderRepository orderRepository;
    private final OrderSubject orderSubject;
    private final OrderProcessingChain orderProcessingChain;
    private final ClientService clientService;
    private final DishService dishService;

    public OrderServiceImpl(IOrderRepository orderRepository, OrderSubject orderSubject, OrderProcessingChain orderProcessingChain, ClientService clientService, DishService dishService) {
        this.orderRepository = orderRepository;
        this.orderSubject = orderSubject;
        this.orderProcessingChain = orderProcessingChain;
        this.clientService = clientService;
        this.dishService = dishService;
    }

    @Override
    @Transactional
    public Order create(OrderRequestDTO dto) {
        Client client = clientService.getById(dto.clientId());
        List<Dish> dishes = dto.dishIds().stream().map(dishService::getById).toList();

        Order order = new Order();
        order.setClient(client);
        order.setDishes(dishes);

        recalculateAndProcess(order);

        Order created = orderRepository.save(order);
        orderSubject.notifyObservers(EventType.CREATE, created);
        return created;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Order order = getById(id);
        orderRepository.delete(order);
        orderSubject.notifyObservers(EventType.DELETE, order);
    }

    @Override
    @Transactional
    public Order update(Long id, OrderRequestDTO dto) {
        Order existing = getById(id);

        Client client = clientService.getById(dto.clientId());
        List<Dish> dishes = dto.dishIds().stream().map(dishService::getById).toList();

        existing.setClient(client);
        existing.setDishes(dishes);

        recalculateAndProcess(existing);

        Order updated = orderRepository.save(existing);
        orderSubject.notifyObservers(EventType.UPDATE, updated);
        return updated;
    }

    private void recalculateAndProcess(Order order) {
        orderProcessingChain.process(order);

        BigDecimal totalPrice = OrderPriceCalculator.calculateTotalPrice(
                order.getDishes(),
                order.getClient().getClientType()
        );
        order.setTotalPrice(totalPrice);
    }

}
