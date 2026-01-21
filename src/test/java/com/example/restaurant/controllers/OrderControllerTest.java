package com.example.restaurant.controllers;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.DishType;
import com.example.restaurant.dto.order.OrderRequestDTO;
import com.example.restaurant.dto.order.OrderResponseDTO;
import com.example.restaurant.models.Menu;
import com.example.restaurant.models.Order;
import com.example.restaurant.models.Client;
import com.example.restaurant.models.Dish;
import com.example.restaurant.services.client.GetClientByIdService;
import com.example.restaurant.services.dish.GetDishByIdService;
import com.example.restaurant.utils.converter.OrderDtoConverter;
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
    private GetClientByIdService getClientByIdService;
    private GetDishByIdService getDishByIdService;
    private CreateOrderService createOrderService;
    private GetOrderByIdService getOrderByIdService;
    private GetAllOrdersService getAllOrdersService;
    private UpdateOrderService updateOrderService;
    private DeleteOrderService deleteOrderService;

    private Order order;
    private Client client;
    private Dish dish;

    @BeforeEach
    void setUp() {
        getClientByIdService = mock(GetClientByIdService.class);
        getDishByIdService = mock(GetDishByIdService.class);
        createOrderService = mock(CreateOrderService.class);
        getOrderByIdService = mock(GetOrderByIdService.class);
        getAllOrdersService = mock(GetAllOrdersService.class);
        updateOrderService = mock(UpdateOrderService.class);
        deleteOrderService = mock(DeleteOrderService.class);

        webTestClient = WebTestClient.bindToController(new OrderController(
                getClientByIdService,
                getDishByIdService,
                createOrderService,
                getOrderByIdService,
                getAllOrdersService,
                updateOrderService,
                deleteOrderService
        )).build();

        Menu menu = new Menu(1L, "Lunch Menu", "Delicious options", new ArrayList<>());
        dish = new Dish(1L, "Pasta", "Delicious pasta", 12.99F, DishType.COMMON, menu);

        client = new Client(1L, "John", "Doe", "john.doe@example.com", ClientType.COMMON);
        order = new Order(client, List.of(dish), 1L, 12.99F);
    }


    @Test
    @DisplayName("Create Order")
    void createOrder() {
        when(getClientByIdService.execute(anyLong())).thenReturn(client);
        when(getDishByIdService.execute(anyLong())).thenReturn(dish);
        when(createOrderService.execute(anyLong(), any())).thenReturn(order);

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

        verify(createOrderService).execute(anyLong(), any());
    }

    @Test
    @DisplayName("Get Order by ID")
    void getOrderById() {
        when(getOrderByIdService.execute(anyLong())).thenReturn(order);

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

        verify(getOrderByIdService).execute(anyLong());
    }

    @Test
    @DisplayName("Get All Orders")
    void getAllOrders() {
        List<Order> orders = List.of(order, new Order(client, List.of(dish), 2L, 25.98F));

        when(getAllOrdersService.execute()).thenReturn(orders);

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

        verify(getAllOrdersService).execute();
    }

    @Test
    @DisplayName("Update Order")
    void updateOrder() {
        when(getClientByIdService.execute(anyLong())).thenReturn(client);
        when(getDishByIdService.execute(anyLong())).thenReturn(dish);
        when(updateOrderService.execute(anyLong(), any(Order.class))).thenReturn(order);

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

        verify(updateOrderService).execute(anyLong(), any(Order.class));
    }

    @Test
    @DisplayName("Delete Order")
    void deleteOrder() {
        doNothing().when(deleteOrderService).execute(anyLong());

        webTestClient.delete()
                .uri("/orders/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteOrderService).execute(anyLong());
    }

        @Test
        void getTotalPrice_handlesNullTotalPrice() {
            Client client = new Client();
            Order order = new Order();
            order.setClient(client);
            order.setDishes(new ArrayList<>());
            order.setTotalPrice(null);

            OrderResponseDTO orderResponseDTO = OrderDtoConverter.toDto(order);

            assertEquals(0.0f, orderResponseDTO.getTotalPrice(), "Total price should be 0.0 when order.getTotalPrice() is null");
        }
    }
