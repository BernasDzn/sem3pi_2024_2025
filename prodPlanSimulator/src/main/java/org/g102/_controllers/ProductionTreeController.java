package org.g102._controllers;

import org.g102.domain.*;
import org.g102.database.JDBC;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102.tree.*;
import org.g102._ui.console.ProductionTreeUI;
import org.g102._ui.console.Register.BSTUI;
import org.g102._ui.console.SimulationUI;

import java.sql.ResultSet;
import java.util.*;

public class ProductionTreeController {

    private final NAryTree<ProductionNode> productionTree;

    public ProductionTreeController(String selectedProduct) {
        productionTree = CSVtoTree.getProductionTree(selectedProduct);
    }

    public String showTree() {
        System.out.println();
        return productionTree.toString();
    }

    public void searchOperationOrMaterial() {

        int selectedOption = InputReader.showListReturnIndex(List.of(
                "By Operation"
                , "By Material"
                , "Go Back"), "Search by Operation or Material");
        switch (selectedOption) {
            case 1:
                searchOperation(productionTree);
                break;
            case 2:
                searchMaterial(productionTree);
                break;
            case 3:
                new ProductionTreeUI().run();
                break;
        }

    }

    private void searchMaterial(NAryTree<ProductionNode> productionTree) {
        List<Material> materials = getMaterials();
        if(!materials.isEmpty()){
            int selectedMaterial = InputReader.showListReturnIndex(materials, "Select Material")-1;
            GenericItem material = new GenericItem(materials.get(selectedMaterial).getPid(), materials.get(selectedMaterial).getName());
            if(productionTree.findNodesOfElement(GenericItem.class).contains(material)){
                NAryTree.Node<ProductionNode> node = productionTree.findNode(productionTree.getRoot(), materials.get(selectedMaterial));
                System.out.println("Node type: " + node.getElement().getClass().getSimpleName());
                if(node.getParent() != null){
                    System.out.println("Parent: " + node.getParent().getElement().toString());
                }else {
                    System.out.println("Parent: Root");
                }
                System.out.println("Quantity: " + node.getWeight());
            }
        }
        searchOperationOrMaterial();
    }

    private void searchOperation(NAryTree<ProductionNode> productionTree) {
        ResultSet rs = JDBC.getListFromTable("Operation");
        List<Operation> operations = new ArrayList<>();
        try {
            if(rs.next()){
                while (rs.next()) {
                    operations.add(new Operation(rs.getInt("operation_id"), rs.getString("description")));
                }
            }else {
                System.out.println("No operations found");
            }
        } catch (Exception e) {
            ConsoleWriter.displayError("Error reading operations");
            e.printStackTrace();
        }
        if(!operations.isEmpty()){
            int selectedOperation = InputReader.showListReturnIndex(operations, "Select Operation");
            NAryTree.Node<ProductionNode> node = productionTree.findNode(productionTree.getRoot(), operations.get(selectedOperation));
            if(node != null){
                System.out.println("Node type: " + node.getElement().getClass().getSimpleName());
                if(node.getParent() != null){
                    System.out.println("Parent: " + node.getParent().getElement().toString());
                }else {
                    System.out.println("Parent: Root");
                }
                System.out.println("Quantity: " + node.getWeight());
            }
        }
        searchOperationOrMaterial();
    }

    public void changeQuantityOfMaterial(Float quantity, Material material) {
        productionTree.findNode(productionTree.getRoot(), new GenericItem(material.getPid(), material.getName())).setWeight(quantity);
    }

    public HeapPriorityQueue<Integer, GenericItem> getPriorityQueue() {
        HeapPriorityQueue<Integer, GenericItem> queue = new HeapPriorityQueue<>();
        List<GenericItem> products = new ArrayList<>();
        List<Integer> priority = new ArrayList<>();
        List<Operation> operations = productionTree.findNodesOfElement(Operation.class).stream().map(op -> (Operation) op).toList();

        for (Operation operation : operations) {
            int l = productionTree.getDepth(productionTree.findNode(productionTree.getRoot(), operation));
            products.add(operation.getOutputProduct());
            priority.add(l);
        }

        for (int i = 0; i < products.size(); i++) {
            queue.insert(priority.get(i), products.get(i));
        }
        return queue;
    }

    public List<Material> getMaterials() {
        List<ProductionNode> elements = productionTree.findNodesOfElement(GenericItem.class);
        List<Material> materials = new ArrayList<>();
        for (ProductionNode element : elements) {
            materials.add(new Material(((GenericItem) element).getPid(), (element).getName()));
        }
        return materials;
    }

    public String displayCriticalPathOperations() {
        HeapPriorityQueue<Integer, ProductionNode> queue = new HeapPriorityQueue<>();
        List<ProductionNode> criticalPath = new ArrayList<>();
        traverseAndInsertOperations(productionTree.getRoot(), 0, new ArrayList<>(), criticalPath,queue);
        StringBuilder sb = new StringBuilder();
        sb.append(queue.toStringCriticalPath(criticalPath));
        return sb.toString();
    }

    private void traverseAndInsertOperations(NAryTree.Node<ProductionNode> node, int depth,
                                             List<ProductionNode> currentPath,
                                             List<ProductionNode> criticalPath, HeapPriorityQueue<Integer, ProductionNode> queue) {

        if (node == null) {
            return;
        }

        currentPath.add(node.getElement());

        if (node.getElement() instanceof Operation) {
            queue.insert(depth, node.getElement());
        }

        if (node.getChildrenNodes().isEmpty()) {
            if (currentPath.size() > criticalPath.size()) {
                criticalPath.clear();
                criticalPath.addAll(currentPath);
            }
        }

        for (NAryTree.Node<ProductionNode> child : node.getChildrenNodes()) {
            traverseAndInsertOperations(child, depth + 1, new ArrayList<>(currentPath), criticalPath, queue);
        }
    }

    public List<ProductionNode> getCriticalPath(HeapPriorityQueue<Integer, ProductionNode> queue) {
        List<ProductionNode> criticalPath = new ArrayList<>();

        while (!queue.isEmpty()) {
            ProductionNode op = queue.removeMin().getValue();
            criticalPath.add(op);
        }
        return criticalPath;
    }

    public void getTotalProductionCost() {
        Map<Material, Float> materialQuantities = new HashMap<>();
        Map<Operation, Float> operationQuantities = new HashMap<>();
        List<Material> materials = getMaterials();
        for (Material material : materials) {
            if(materialQuantities.containsKey(material)) {
                materialQuantities.put(material, materialQuantities.get(material)+getMaterialQuantity(material));
            }
            else {
                materialQuantities.put(material, getMaterialQuantity(material));
            }
        }
        List<Operation> operations = productionTree.findNodesOfElement(Operation.class).stream().map(op -> (Operation) op).toList();
        for(Operation operation : operations){
            if(productionTree.findNode(productionTree.getRoot(), operation).getParent() != null)
                operationQuantities.put(operation, productionTree.findNode(productionTree.getRoot(), operation).getWeight());
            else
                operationQuantities.put(operation, 1f);
        }
        ConsoleWriter.displayHeader("Materials and Operations quantities:");
        ConsoleWriter.displayHeader("Materials:");
        for(Map.Entry<Material, Float> entry : materialQuantities.entrySet()){
            System.out.println(entry.getValue()+"x " + entry.getKey().getName());
        }
        ConsoleWriter.displayHeader("Operations:");
        for(Map.Entry<Operation, Float> entry : operationQuantities.entrySet()){
            System.out.println(entry.getValue()+"x " + entry.getKey().getName());
        }
        System.out.println();
    }

    public float getMaterialQuantity(Material material) {
        float quantity = productionTree.findNode(productionTree.getRoot(), new GenericItem(material.getPid(), material.getName())).getWeight();
        Operation operation = (Operation) productionTree.findNode(productionTree.getRoot(), material).getParent().getElement();
        while (productionTree.findNode(productionTree.getRoot(), operation).getParent() != null) {
            quantity *= productionTree.findNode(productionTree.getRoot(), operation).getWeight();
            operation = (Operation) productionTree.findNode(productionTree.getRoot(), operation).getParent().getElement();
        }
        return quantity;
    }

    /**
     * AC01: As a user, when I organize the operations in the system, it uses an AVL tree to store the
     * operations by their dependency levels, creating an intricate hierarchy of operations.
     * AC02: As a user, when I traverse this AVL tree, I can efficiently extract and prioritize operations
     * for production in the simulator.
     */
    public void runSimulator() {
        AVL<Operation> avl = new AVL<>();
        List<Operation> operations = productionTree.findNodesOfElement(Operation.class).stream().map(op -> (Operation) op).toList();
        for (Operation operation : operations) {
            avl.insert(operation);
        }
        Map<Integer, List<Operation>> level = new HashMap<>();
        for (Operation operation : operations) {
            int l = productionTree.getDepth(productionTree.findNode(productionTree.getRoot(), operation));
            if (level.containsKey(l)) {
                level.get(l).add(operation);
            } else {
                level.put(l, new ArrayList<>(List.of(operation)));
            }
        }
        for (int i = level.size()-1; i >= 0; i--) {
            ConsoleWriter.displayHeader("Level " + i);
            List<Item> items = new ArrayList<>();
            int id = 0;
            for (Operation operation : level.get(i)) {
                items.add(new Item(id, ItemPriority.NORMAL, new LinkedList<>(List.of(operation.getName()))));
                id++;
            }
            List<Machine> machines = new ArrayList<>();
            id = 0;
            for (Operation operation : level.get(i)) {
                machines.add(new Machine("ws_" + id, operation.getName(), 10));
                id++;
            }
            SimulationUI simulationUI = new SimulationUI(machines, items);
        }
        System.out.println();
    }

    public void listMaterialsUsedInProduct() {
        BSTUI bstUI = new BSTUI();
        bstUI.run();
    }
}