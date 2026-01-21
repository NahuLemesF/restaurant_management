package com.example.restaurant.services.order;

import com.example.restaurant.constants.EventType;
import com.example.restaurant.dto.order.OrderRequestDTO;
import com.example.restaurant.handlers.OrderProcessingChain;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.models.Order;
import com.example.restaurant.observers.OrderSubject;
import com.example.restaurant.repositories.IOrderRepository;
import com.example.restaurant.services.client.GetClientByIdService;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.utils.OrderPriceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final IOrderRepository orderRepository;
    private final GetClientByIdService getClientByIdService;
    private final GetDishByIdService getDishByIdService;
    private final OrderSubject orderSubject;
    private final OrderProcessingChain orderProcessingChain;

    public OrderServiceImpl(IOrderRepository orderRepository, GetClientByIdService getClientByIdService, GetDishByIdService getDishByIdService, OrderSubject orderSubject, OrderProcessingChain orderProcessingChain) {
        this.orderRepository = orderRepository;
        this.getClientByIdService = getClientByIdService;
        this.getDishByIdService = getDishByIdService;
        this.orderSubject = orderSubject;
        this.orderProcessingChain = orderProcessingChain;
    }

    @Override
    public Order create(OrderRequestDTO dto) {
        Client client = getClientByIdService.execute(dto.getClientId());
        List<Dish> dishes = dto.getDishIds().stream().map(getDishByIdService::execute).toList();

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
                .orElseThrow(() -> new RuntimeException("Orden con el id " + id + " no encontrada"));
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

        Client client = getClientByIdService.execute(dto.getClientId());
        List<Dish> dishes = dto.getDishIds().stream().map(getDishByIdService::execute).toList();

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
