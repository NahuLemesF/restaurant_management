package com.example.restaurant.services.order;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.order.OrderRequestDTO;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.handlers.OrderProcessingChain;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Order;
import com.example.restaurant.observers.OrderSubject;
import com.example.restaurant.repositories.IOrderRepository;
import com.example.restaurant.services.client.ClientService;
import com.example.restaurant.services.dish.DishService;
import com.example.restaurant.utils.OrderPriceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Order create(OrderRequestDTO dto) {
        Client client = clientService.getById(dto.getClientId());
        List<Dish> dishes = dto.getDishIds().stream().map(dishService::getById).toList();

        Order order = new Order();
        order.setClient(client);
        order.setDishes(dishes);
        order.setOrderDate(LocalDateTime.now());

        recalculateAndProcess(order);

        Order created = orderRepository.save(order);
        orderSubject.notifyObservers(EventType.CREATE, created);
        return created;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Order order = getById(id);
        orderRepository.delete(order);
        orderSubject.notifyObservers(EventType.DELETE, order);
    }

    @Override
    public Order update(Long id, OrderRequestDTO dto) {
        Order existing = getById(id);

        Client client = clientService.getById(dto.getClientId());
        List<Dish> dishes = dto.getDishIds().stream().map(dishService::getById).toList();

        existing.setClient(client);
        existing.setDishes(dishes);

        recalculateAndProcess(existing);

        Order updated = orderRepository.save(existing);
        orderSubject.notifyObservers(EventType.UPDATE, updated);
        return updated;
    }

    private void recalculateAndProcess(Order order) {
        orderProcessingChain.process(order);

        float totalPrice = OrderPriceCalculator.calculateTotalPrice(
                order.getDishes(),
                order.getClient().getClientType()
        );
        order.setTotalPrice(totalPrice);
    }

}
