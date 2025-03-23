package com.kunals990.secureorder.rest.DTO;

import com.kunals990.secureorder.order.Order;
import com.kunals990.secureorder.user.User;

import java.time.Instant;
import java.util.List;

public record UserDto(Long id, String username, String name, String email, String role, List<OrderDto> orders) {

    public record OrderDto(String id, String description, Instant createdAt) {

        public static OrderDto from(Order order) {
            return new OrderDto(order.getId(), order.getDescription(), order.getCreatedAt());
        }
    }

    public static UserDto from(User user) {
        List<OrderDto> orders = user.getOrders().stream()
                .map(OrderDto::from)
                .toList();

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                orders
        );
    }
}