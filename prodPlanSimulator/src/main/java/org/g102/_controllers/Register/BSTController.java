package org.g102._controllers.Register;

import org.g102.domain.GenericItem;
import org.g102.tree.BST;
import org.g102.tree.CSVtoTree;
import org.g102.tree.QuantityNode;

import java.util.ArrayList;
import java.util.HashMap;

public class BSTController {
    private BST<QuantityNode<GenericItem>> binarySearchTree;

    public BSTController() {
        binarySearchTree= CSVtoTree.getMaterialQuantityList();
    }

    public String showAscendingOrder(){
        HashMap<Float, ArrayList<GenericItem>> materialInAscendingOrder= new HashMap<>();
        materialInAscendingOrder= binarySearchTree.showAscendingOrder(materialInAscendingOrder);
        return binarySearchTree.printMaterials(materialInAscendingOrder);
    }

    public String showDescendingOrder(){
        HashMap<Float, ArrayList<GenericItem>> materialInDescendingOrder= new HashMap<>();
        materialInDescendingOrder= binarySearchTree.showDescendingOrder(materialInDescendingOrder);
        return binarySearchTree.printMaterials(materialInDescendingOrder);
    }
}
