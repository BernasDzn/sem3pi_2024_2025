package org.g102.domain;

import org.g102.domain.Item;
import org.g102.domain.ItemPriority;
import org.g102.domain.Machine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<String>(){{
        add("Cutting");
        add("Welding");
    }}, new double[]{1.0, 2.0, 3.0}, "Red",25);

    public boolean compareLL(LinkedList<String> l1, LinkedList<String> l2){
        if (l1.size() == l2.size()){
            for(String o : l1){
                if(!l2.contains(o)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, item.getId());
    }

    @Test
    void getPriority() {
        Assertions.assertEquals(ItemPriority.HIGH, item.getPriority());
    }

    @Test
    void getOperations() {
        LinkedList<String> operations = new LinkedList<String>(){{
            add("Cutting");
            add("Welding");
        }};

        Assertions.assertTrue(compareLL(operations, item.getOperations()));
    }

    @Test
    void getSize() {
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, item.getSize());
    }

    @Test
    void getNextOperation() {
        Assertions.assertEquals(0, item.getNextOperation());
    }

    @Test
    void getColor() {
        Assertions.assertEquals("Red", item.getColor());
    }

    @Test
    void setId() {
        item.setId(2);
        Assertions.assertEquals(2, item.getId());
    }

    @Test
    void setPriority() {
        item.setPriority(ItemPriority.NORMAL);
        Assertions.assertEquals(ItemPriority.NORMAL, item.getPriority());
    }

    @Test
    void setOperations() {
        item.setOperations(new LinkedList<>(){{
            add("Drilling");
            add("Painting");
        }});

        LinkedList<String> operations = new LinkedList<String>(){{
           add("Drilling");
           add("Painting");
        }};

        Assertions.assertTrue(compareLL(operations, item.getOperations()));
    }

    @Test
    void setSize() {
        item.setSize(new double[]{2.0, 3.0, 4.0});
        assertArrayEquals(new double[]{2.0, 3.0, 4.0}, item.getSize());
    }

    @Test
    void setColor() {
        item.setColor("Blue");
        Assertions.assertEquals("Blue", item.getColor());
    }

    @Test
    void setNextOperation() {
        item.setNextOperation(1);
        Assertions.assertEquals(1, item.getNextOperation());
    }

    @Test
    void itemShouldInitializeWithCorrectValues() {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<>(Arrays.asList("Cutting", "Welding")), new double[]{1.0, 2.0, 3.0}, "Red", 25);
        assertEquals(1, item.getId());
        assertEquals(ItemPriority.HIGH, item.getPriority());
        assertEquals(Arrays.asList("Cutting", "Welding"), item.getOperations());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, item.getSize());
        assertEquals("Red", item.getColor());
        assertEquals(0, item.getNextOperation());
        assertEquals(25, item.getProdTime());
    }

    @Test
    void itemShouldThrowExceptionForInvalidSize() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Item(1, ItemPriority.HIGH, new LinkedList<>(Arrays.asList("Cutting", "Welding")), new double[]{1.0, 2.0}, "Red", 25);
        });
        assertEquals("Size must have 3 dimensions", exception.getMessage());
    }

    @Test
    void itemShouldUpdateProductionTime() {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<>(Arrays.asList("Cutting", "Welding")), new double[]{1.0, 2.0, 3.0}, "Red", 25);
        item.setProductionTime(30);
        assertEquals(30, item.getProdTime());
    }

    @Test
    void itemShouldAddMachineUsed() {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<>(Arrays.asList("Cutting", "Welding")), new double[]{1.0, 2.0, 3.0}, "Red", 25);
        Machine machine = new Machine("M1", "Cutting", 10);
        item.addMachineUsed(machine);
        assertEquals(1, item.getMachinesUsed().size());
        assertEquals(machine, item.getMachinesUsed().get(0));
    }

    @Test
    void itemShouldHandleEmptyOperationsList() {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<>(), new double[]{1.0, 2.0, 3.0}, "Red", 25);
        assertTrue(item.getOperations().isEmpty());
    }

    @Test
    void itemShouldUpdateMachinesUsed() {
        Item item = new Item(1, ItemPriority.HIGH, new LinkedList<>(Arrays.asList("Cutting", "Welding")), new double[]{1.0, 2.0, 3.0}, "Red", 25);
        List<Machine> machines = Arrays.asList(new Machine("M1", "Cutting", 10), new Machine("M2", "Welding", 15));
        item.setMachinesUsed(machines);
        assertEquals(2, item.getMachinesUsed().size());
        assertEquals(machines, item.getMachinesUsed());
    }
}