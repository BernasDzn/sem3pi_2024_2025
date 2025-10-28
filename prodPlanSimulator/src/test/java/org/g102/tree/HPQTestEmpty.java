package org.g102.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HPQTestEmpty {

    HeapPriorityQueue<Integer, String> instance;

    Integer[] keys = {};
    String[] values = {};

    @BeforeEach
    public void setUp() {
        instance = new HeapPriorityQueue<>(keys, values);
    }


    @Test
    public void testConstructor() {
        System.out.println("constructor");

        assertEquals(0, instance.size());
    }


    @Test
    public void testParent() {
        System.out.println("parent");
        assertEquals(3, instance.parent(8));
        assertEquals(0, instance.parent(2));
    }


    @Test
    public void testLeft() {
        System.out.println("left");
        int j = 4;

        int expResult = 9;
        int result = instance.left(j);
        assertEquals(expResult, result);

    }

    @Test
    public void testRight() {
        System.out.println("right");
        int j = 1;

        int expResult = 4;
        int result = instance.right(j);
        assertEquals(expResult, result);

    }

    @Test
    public void testSize() {
        System.out.println("size");

        assertEquals(0, instance.size());
    }

    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");

        assertEquals(true, instance.isEmpty());
    }

    @Test
    public void testMin() {
        System.out.println("min");

        assertEquals(null, instance.min());
    }

    @Test
    public void testInsert() {
        System.out.println("insert");
        Integer key = 1;
        String value = "A";

        instance.insert(key, value);

        assertEquals(1, instance.size());
        assertEquals("A", instance.min().getValue());
    }

    @Test
    public void testRemoveMin() {
        System.out.println("removeMin");

        assertEquals(null, instance.removeMin());
    }
}
