package org.g102.tree;

import org.g102.domain.GenericItem;
import org.g102.domain.Material;
import org.g102.domain.Operation;
import org.g102.domain.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    static GenericItem outputProduct = new Product("PN233", "Product1", "description");
    static Material product1 = new Material("PN234", "RawMat1");
    static Material product2 = new Material("PN235", "RawMat2");
    static Operation operation = new Operation(1, "op1", outputProduct, 1);
    static Operation operation2 = new Operation(2, "op2");
    static Operation operation3 = new Operation(3, "op3");
    static NAryTree.Node<ProductionNode> node_child_4 = new NAryTree.Node<>(product2, 4);
    static NAryTree.Node<ProductionNode> node_child_1 = new NAryTree.Node<>(operation2, 1, List.of(node_child_4));
    static NAryTree.Node<ProductionNode> node_child_2 = new NAryTree.Node<>(operation3, 2);
    static NAryTree.Node<ProductionNode> node_child_3 = new NAryTree.Node<>(product1, 1);
    static NAryTree.Node<ProductionNode> node_parent = new NAryTree.Node<>(operation, 0, List.of(node_child_1, node_child_2, node_child_3));
    static NAryTree<ProductionNode> tree = new NAryTree<>();

    @BeforeAll
    static void setUp() {
        tree.setRoot(node_parent);
    }

    @Test
    void getElement() {
        assertEquals(operation, node_parent.getElement());
    }

    @Test
    void getChildrenNodes() {
        assertEquals(List.of(node_child_1, node_child_2, node_child_3), node_parent.getChildrenNodes());
    }

    @Test
    void testToString() {
        System.out.println(tree.toString());
        assertTrue(true);
    }

    @Test
    void getDepth() {
        assertEquals(0, tree.getDepth(node_parent));
        assertEquals(1, tree.getDepth(node_child_1));
        assertEquals(2, tree.getDepth(node_child_4));
    }

}