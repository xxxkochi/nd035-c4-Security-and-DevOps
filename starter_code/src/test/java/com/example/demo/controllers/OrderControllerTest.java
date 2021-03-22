package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static  org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void test_submit_order() {
        when(userRepository.findByUsername("testUser")).thenReturn(createTestUser());

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals("testUser", userOrder.getUser().getUsername());
        assertEquals(3, userOrder.getUser().getCart().getItems().size());
    }

    @Test
    public void test_get_orders_for_user() {
        UserOrder userOrder=createTestOrder();
        List<UserOrder> orders = new ArrayList<>(Arrays.asList(userOrder, userOrder, userOrder));
        when(userRepository.findByUsername("testUser")).thenReturn(userOrder.getUser());
        when(orderRepository.findByUser(userOrder.getUser())).thenReturn(orders);
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> actualOrders = response.getBody();
        assertNotNull(actualOrders);
        assertEquals(new BigDecimal(9), actualOrders.get(1).getTotal());
    }

    public User createTestUser() {
        Cart cart = new Cart();
        User user = new User();

        user.setId(1l);
        user.setUsername("testUser");
        user.setPassword("qwerty123");

        List<Item> items = new ArrayList<Item>();
        for (Long j = 0L; j < 3L; j++) {
            Item item = new Item();
            item.setId(j);
            item.setDescription("test");
            item.setName("test");
            item.setPrice(new BigDecimal(3));
            items.add(item);
        }

        cart.setItems(items);
        cart.setId(1l);
        cart.setUser(user);
        cart.setTotal(new BigDecimal(9));
        user.setCart(cart);
        return user;
    }

    public UserOrder createTestOrder() {
        UserOrder userOrder=new UserOrder();
        User user = createTestUser();
        userOrder.setId(1l);
        userOrder.setUser(user);
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(user.getCart().getTotal());
        return userOrder;
    }



}
