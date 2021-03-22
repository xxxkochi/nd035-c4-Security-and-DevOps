package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static  org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testItem");
        item.setPrice(new BigDecimal(99.99));
        return item;
    }

    @Test
    public void test_get_item_by_id() {
        Item item = createItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item actualItem = response.getBody();
        assertNotNull(actualItem);
        assertEquals("testItem", actualItem.getName());
    }

    @Test
    public void test_get_items_by_name() {
        Item item = createItem();
        List<Item> items = new ArrayList<>(Arrays.asList(item, item, item));
        when(itemRepository.findByName("testItem")).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        items = response.getBody();
        assertNotNull(items);
        assertEquals("testItem", items.get(0).getName());
        assertEquals(3,items.size());
    }

    @Test
    public void test_get_all_items() {
        Item item = createItem();
        List<Item> items = new ArrayList<>(Arrays.asList(item, item, item));
        when(itemRepository.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        items = response.getBody();
        assertNotNull(items);
        assertEquals(item.getPrice(), items.get(1).getPrice());
        assertEquals(3,items.size());
    }

}
