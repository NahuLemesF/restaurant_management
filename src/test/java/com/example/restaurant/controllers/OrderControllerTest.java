package com.example.restaurant.controllers;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.DishType;
import com.example.restaurant.dto.order.OrderRequestDTO;
import com.example.restaurant.dto.order.OrderResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.models.Order;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.services.order.OrderService;
import com.example.restaurant.utils.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

class OrderControllerTest {

    private WebTestClient webTestClient;
    private OrderService orderService;

    private Order order;
    private Client client;
    private Dish dish;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);

        webTestClient = WebTestClient.bindToController(new OrderController(
                orderService
        )).build();

        Menu menu = new Menu(1L, "Lunch Menu", "Delicious options", new ArrayList<>());
        dish = new Dish(1L, "Pasta", "Delicious pasta", 12.99F, DishType.COMMON, menu);

        client = new Client(1L, "John", "Doe", "john.doe@example.com", ClientType.COMMON);
        order = new Order(client, List.of(dish), 1L, 12.99F);
    }


    @Test
    @DisplayName("Create Order")
    void createOrder() {
        when(orderService.create(any(OrderRequestDTO.class))).thenReturn(order);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setDishIds(List.of(1L));

        webTestClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(order.getClient().getName(), response.getClient().getName());
                    assertEquals(order.getDishes().get(0).getName(), response.getDishes().get(0).getName());
                    assertEquals(order.getTotalPrice(), response.getTotalPrice());
                });

        verify(orderService).create(any(OrderRequestDTO.class));
    }

    @Test
    @DisplayName("Get Order by ID")
    void getOrderById() {
        when(orderService.getById(anyLong())).thenReturn(order);

        webTestClient.get()
                .uri("/orders/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(order.getClient().getName(), response.getClient().getName());
                    assertEquals(order.getDishes().get(0).getName(), response.getDishes().get(0).getName());
                    assertEquals(order.getTotalPrice(), response.getTotalPrice());
                });

        verify(orderService).getById(anyLong());
    }

    @Test
    @DisplayName("Get All Orders")
    void getAllOrders() {
        List<Order> orders = List.of(order, new Order(client, List.of(dish), 2L, 25.98F));

        when(orderService.getAll()).thenReturn(orders);

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(OrderResponseDTO.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals(order.getId(), response.get(0).getId());
                    assertEquals(2L, response.get(1).getId());
                });

        verify(orderService).getAll();
    }

    @Test
    @DisplayName("Update Order")
    void updateOrder() {
        when(orderService.update(anyLong(), any(OrderRequestDTO.class))).thenReturn(order);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setDishIds(List.of(1L));

        webTestClient.put()
                .uri("/orders/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(order.getClient().getName(), response.getClient().getName());
                    assertEquals(order.getDishes().get(0).getName(), response.getDishes().get(0).getName());
                    assertEquals(order.getTotalPrice(), response.getTotalPrice());
                });

        verify(orderService).update(anyLong(), any(OrderRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Order")
    void deleteOrder() {
        doNothing().when(orderService).delete(anyLong());

        webTestClient.delete()
                .uri("/orders/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).delete(anyLong());
    }

        @Test
        void getTotalPrice_handlesNullTotalPrice() {
            Client client = new Client();
            Order order = new Order();
            order.setClient(client);
            order.setDishes(new ArrayList<>());
            order.setTotalPrice(null);

            OrderResponseDTO orderResponseDTO = OrderMapper.toDto(order);

            assertEquals(0.0f, orderResponseDTO.getTotalPrice(), "Total price should be 0.0 when order.getTotalPrice() is null");
        }
    }
