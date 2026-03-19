package com.nahulemes.restaurantmanagement.controllers;

import com.nahulemes.restaurantmanagement.constants.ClientType;
import com.nahulemes.restaurantmanagement.constants.DishType;
import com.nahulemes.restaurantmanagement.dto.order.OrderRequestDTO;
import com.nahulemes.restaurantmanagement.dto.order.OrderResponseDTO;
import com.nahulemes.restaurantmanagement.models.Menu;
import com.nahulemes.restaurantmanagement.models.Order;
import com.nahulemes.restaurantmanagement.models.Client;
import com.nahulemes.restaurantmanagement.models.Dish;
import com.nahulemes.restaurantmanagement.services.order.OrderService;
import com.nahulemes.restaurantmanagement.utils.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;
    private OrderService orderService;
    private ObjectMapper objectMapper;

    private Order order;
    private Client client;
    private Dish dish;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();

        Menu menu = new Menu(1L, "Lunch Menu", "Delicious options", new ArrayList<>());
        dish = new Dish(1L, "Pasta", "Delicious pasta", new BigDecimal("12.99"), DishType.COMMON, menu);

        client = new Client(1L, "John", "Doe", "john.doe@example.com", ClientType.COMMON);
        order = new Order(client, List.of(dish), 1L, new BigDecimal("12.99"));
    }


    @Test
    @DisplayName("Create Order")
    void createOrder() throws Exception {
        when(orderService.create(any(OrderRequestDTO.class))).thenReturn(order);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(1L, List.of(1L));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.client.name").value(order.getClient().getName()))
                .andExpect(jsonPath("$.dishes[0].name").value(order.getDishes().get(0).getName()))
                .andExpect(jsonPath("$.totalPrice").value(12.99));

        verify(orderService).create(any(OrderRequestDTO.class));
    }

    @Test
    @DisplayName("Get Order by ID")
    void getOrderById() throws Exception {
        when(orderService.getById(anyLong())).thenReturn(order);

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.client.name").value(order.getClient().getName()))
                .andExpect(jsonPath("$.dishes[0].name").value(order.getDishes().get(0).getName()))
                .andExpect(jsonPath("$.totalPrice").value(order.getTotalPrice()));

        verify(orderService).getById(anyLong());
    }

    @Test
    @DisplayName("Get All Orders")
    void getAllOrders() throws Exception {
        List<Order> orders = List.of(order, new Order(client, List.of(dish), 2L, new BigDecimal("25.98")));

        when(orderService.getAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(orderService).getAll();
    }

    @Test
    @DisplayName("Update Order")
    void updateOrder() throws Exception {
        when(orderService.update(anyLong(), any(OrderRequestDTO.class))).thenReturn(order);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(1L, List.of(1L));

        mockMvc.perform(put("/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.client.name").value(order.getClient().getName()))
                .andExpect(jsonPath("$.dishes[0].name").value(order.getDishes().get(0).getName()))
                .andExpect(jsonPath("$.totalPrice").value(12.99));

        verify(orderService).update(anyLong(), any(OrderRequestDTO.class));
    }

    @Test
    @DisplayName("Delete Order")
    void deleteOrder() throws Exception {
        doNothing().when(orderService).delete(anyLong());

        mockMvc.perform(delete("/orders/{id}", 1L))
                .andExpect(status().isNoContent());

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

        assertEquals(BigDecimal.ZERO, orderResponseDTO.totalPrice(), "Total price should be 0 when order.getTotalPrice() is null");
    }
}
