package com.example.restaurant.utils;

import com.example.restaurant.constants.ClientType;
import com.example.restaurant.constants.DishType;
import com.example.restaurant.models.Dish;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class OrderPriceCalculator {

    private static final Map<DishType, BigDecimal> DISH_TYPE_MULTIPLIER = Map.of(
            DishType.POPULAR, new BigDecimal("1.0573"),  // 5.73% markup for popular dishes
            DishType.COMMON, BigDecimal.ONE
    );

    private static final Map<ClientType, BigDecimal> CLIENT_TYPE_DISCOUNT = Map.of(
            ClientType.FREQUENT, new BigDecimal("0.9762"),  // 2.38% discount for frequent clients
            ClientType.COMMON, BigDecimal.ONE
    );

    public static BigDecimal calculateTotalPrice(List<Dish> dishes, ClientType clientType) {
        BigDecimal totalPrice = dishes.stream()
                .map(OrderPriceCalculator::priceBasedOnDishType)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return priceBasedOnClientType(clientType, totalPrice)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal priceBasedOnDishType(Dish dish) {
        BigDecimal multiplier = DISH_TYPE_MULTIPLIER.getOrDefault(dish.getDishType(), BigDecimal.ONE);
        return dish.getPrice().multiply(multiplier);
    }

    private static BigDecimal priceBasedOnClientType(ClientType clientType, BigDecimal totalPrice) {
        BigDecimal discount = CLIENT_TYPE_DISCOUNT.getOrDefault(clientType, BigDecimal.ONE);
        return totalPrice.multiply(discount);
    }
}
