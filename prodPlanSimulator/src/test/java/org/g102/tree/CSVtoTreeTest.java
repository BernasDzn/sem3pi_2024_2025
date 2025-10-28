package org.g102.tree;

import org.junit.jupiter.api.Test;

class CSVtoTreeTest {

    @Test
    void testRun() {
        NAryTree tree = CSVtoTree.getProductionTree("AS12947S22");
        System.out.println(tree);
    }

}