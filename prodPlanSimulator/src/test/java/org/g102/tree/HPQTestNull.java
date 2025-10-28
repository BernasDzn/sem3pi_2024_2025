package org.g102.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HPQTestNull {

        HeapPriorityQueue<Integer, String> instance;

        Integer[] keys = {};
        String[] values = {};

        @BeforeEach
        public void setUp() {
            instance = new HeapPriorityQueue<>(keys, values);
        }

        @Test
        public void testBuildHeapNull(){
            System.out.println("buildHeapNull");
            assertThrows(IllegalArgumentException.class, () -> instance = new HeapPriorityQueue<Integer,String>(null, List.of("a","b","c")));
        }

        @Test
        public void testInsertNull() {
            System.out.println("insertNull");
            assertThrows(IllegalArgumentException.class, () -> instance.insert(null, "null"));
        }

        @Test
        public void testSize() {
            System.out.println("size");

            assertEquals(0, instance.size());
        }
}
