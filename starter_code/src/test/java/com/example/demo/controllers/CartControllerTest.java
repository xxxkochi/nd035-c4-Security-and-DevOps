package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Optional;

import static  org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void test_add_to_cart() {
        when(userRepository.findByUsername("testUser")).thenReturn(createTestUser());
        when(itemRepository.findById(1l)).thenReturn(Optional.of(createTestItem()));
        final ResponseEntity<Cart> responseEntity = cartController.addTocart(createModifyCartRequest());


        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        assertNotNull(cart);
        assertEquals(5, cart.getItems().size());
        assertEquals("testUser", cart.getUser().getUsername());
    }



    @Test
    public void test_remove_from_cart() {
        when(userRepository.findByUsername("testUser")).thenReturn(createTestUser());
        when(itemRepository.findById(1l)).thenReturn(Optional.of(createTestItem()));
        final ResponseEntity<Cart> responseEntity = cartController.removeFromcart(createModifyCartRequest());


        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
        assertEquals("testUser", cart.getUser().getUsername());
    }



    public User createTestUser() {
        Cart cart = new Cart();
        User user = new User();
        user.setId(1l);
        user.setUsername("testUser");
        user.setPassword("qwerty123");
        cart.setId(1l);
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testItem");
        item.setPrice(new BigDecimal(99.99));
        return item;
    }

    public ModifyCartRequest createModifyCartRequest() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setUsername("testUser");

        return modifyCartRequest;
    }



}
