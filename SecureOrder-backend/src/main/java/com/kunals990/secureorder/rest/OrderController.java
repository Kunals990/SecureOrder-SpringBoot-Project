package com.kunals990.secureorder.rest;

import com.kunals990.secureorder.order.Order;
import com.kunals990.secureorder.order.OrderService;
import com.kunals990.secureorder.rest.DTO.CreateOrderRequest;
import com.kunals990.secureorder.rest.DTO.OrderDto;
import com.kunals990.secureorder.security.CustomUserDetails;
import com.kunals990.secureorder.user.User;
import com.kunals990.secureorder.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kunals990.secureorder.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public List<OrderDto> getOrders(@RequestParam(value = "text", required = false) String text) {
        List<Order> orders = (text == null) ? orderService.getOrders() : orderService.getOrdersContainingText(text);
        return orders.stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(@AuthenticationPrincipal CustomUserDetails currentUser,
                                @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Order order = Order.from(createOrderRequest);
        order.setId(UUID.randomUUID().toString());
        order.setUser(user);
        return OrderDto.from(orderService.saveOrder(order));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    public OrderDto deleteOrders(@PathVariable UUID id) {
        Order order = orderService.validateAndGetOrder(id.toString());
        orderService.deleteOrder(order);
        return OrderDto.from(order);
    }
}
