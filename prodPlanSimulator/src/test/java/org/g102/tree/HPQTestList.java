package org.g102.tree;
import org.g102.domain.GenericItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HPQTestList {
    HeapPriorityQueue<Integer,GenericItem> instance;

    Integer[] keys = {7,6,3,5,2,4,40,0,1,8};
    GenericItem[] values = {new GenericItem("P7","Sete"), new GenericItem("P6","Seis"), new GenericItem("P3","Tres"), new GenericItem("P5","Cinco"), new GenericItem("P2","Dois"), new GenericItem("P4","Quatro"), new GenericItem("P40","Quarenta"), new GenericItem("P0","Zero"), new GenericItem("P1","Um"), new GenericItem("P8","Oito")};

    @BeforeEach
    public void setUp() {
        instance = new HeapPriorityQueue<>(keys,values);
    }

    @Test
    public void testParent() {
        System.out.println("parent");

        assertEquals(instance.parent(8), 3);
        assertEquals(instance.parent(2), 0);
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
        assertEquals(10, instance.size());
    }

    @Test
    public void testHasLeft() {
        System.out.println("hasLeft");
        int j = 4;

        boolean expResult = true;
        boolean result = instance.hasLeft(j);
        assertEquals(expResult, result);
    }

    @Test
    public void testHasRight() {
        System.out.println("hasRight");
        int j = 1;

        boolean expResult = true;
        boolean result = instance.hasRight(j);
        assertEquals(expResult, result);
    }

    @Test
    public void testSwap() {
        System.out.println("swap");
        int i = 1;
        int j = 2;

        instance.swap(i, j);
        assertEquals(3, instance.heap.get(i).getKey());
        assertEquals(1, instance.heap.get(j).getKey());
    }

    @Test
    public void testPercolateUp() {
        System.out.println("percolateUp");

        instance.insert(7, new GenericItem("P7","Sete"));

        List<Integer> keyshp = List.of(0,1,3,5,2,4,40,7,6,8,7);

        for (int i=0; i < instance.size(); i++){
            assertEquals(keyshp.get(i),instance.heap.get(i).getKey());
        }
    }

    @Test
    public void testPercolateDown() {
        System.out.println("percolateDown");

        Entry<Integer, GenericItem> pq = instance.removeMin();
        List<Integer> keyshp = List.of(1,2,3,5,8,4,40,7,6);

        for (int i=0; i < instance.size(); i++){
            assertEquals(keyshp.get(i),instance.heap.get(i).getKey());
        }
    }

    @Test
    public void testBuildHeap() {
        System.out.println("buildHeap");

        List<Integer> keyshp = List.of(0,1,3,5,2,4,40,7,6,8);

        for (int i=0; i < instance.size(); i++){
            assertEquals(keyshp.get(i),instance.heap.get(i).getKey());
        }
    }

    @Test
    public void testMin() {
        System.out.println("min");

        assertEquals(0, instance.min().getKey());
    }

    @Test
    public void testRemoveMin() {
        System.out.println("removeMin");

        assertEquals(0, instance.removeMin().getKey());
        assertEquals(1, instance.min().getKey());
    }

    @Test
    public void testInsert() {
        System.out.println("insert");

        instance.insert(-1, new GenericItem("P9","Nove"));
        assertEquals(-1, instance.min().getKey());
    }

}