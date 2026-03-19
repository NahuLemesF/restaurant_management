package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.dto.order.OrderRequestDTO;
import com.nahulemes.restaurantmanagement.dto.order.OrderResponseDTO;
import com.nahulemes.restaurantmanagement.services.order.OrderService;
import com.nahulemes.restaurantmanagement.utils.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(@RequestBody @Valid OrderRequestDTO dto) {
        return OrderMapper.toDto(orderService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderMapper.toDto(orderService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAll().stream().map(OrderMapper::toDto).toList()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequestDTO dto) {
        return ResponseEntity.ok(OrderMapper.toDto(orderService.update(id, dto)));
    }
}
