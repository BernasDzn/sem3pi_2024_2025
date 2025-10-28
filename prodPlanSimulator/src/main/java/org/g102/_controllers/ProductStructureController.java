package org.g102._controllers;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import org.g102.domain.Operation;
import org.g102.tree.CSVtoTree;
import org.g102.tree.NAryTree;
import org.g102.tree.ProductionNode;
import org.g102._ui.console.ProductStructureUI;

import javax.print.attribute.HashPrintJobAttributeSet;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProductStructureController {

    String path = "resources/product_structures/";
    FileWriter fileWriter;

    public void visualizeBOO(String productID) {
        NAryTree<ProductionNode> tree = new NAryTree<>();
        tree = CSVtoTree.getProductionTree(productID);

        String fileName = productID.toLowerCase().replace(" ", "_") + "_BOO.puml";
        File file = new File(path + fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("@startuml\n");
            fileWriter.write("digraph BOO {\n");
            int count = 0;
            writeNodes(fileWriter, tree.getRoot(), null);
            fileWriter.write("}\n");
            fileWriter.write("@endumlp\n");
            fileWriter.close();
            if(pumlToPng(file.getAbsolutePath())){
                System.out.println("Product structure diagram generated successfully.");
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ProductStructureUI().run();

    }

    public void visualizeBOM(String productID) {
        NAryTree<ProductionNode> tree = new NAryTree<>();
        tree = CSVtoTree.getProductionTree(productID);

        String fileName = productID.toLowerCase().replace(" ", "_") + "_BOM.puml";
        File file = new File(path + fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write("@startuml\n");
            fileWriter.write("digraph BOM {\n");
            writeNodesBOM(fileWriter, tree.getRoot(), null);
            fileWriter.write("}\n");
            fileWriter.write("@endumlp\n");
            fileWriter.close();
            if(pumlToPng(file.getAbsolutePath())){
                System.out.println("Product structure diagram generated successfully.");
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ProductStructureUI().run();
    }

    private void writeNodesBOM(FileWriter fileWriter, NAryTree.Node<ProductionNode> node, String parent_name) throws IOException {
        String node_name = parent_name;
        if (node.getElement() instanceof Operation) {
            if(parent_name != null){
                node_name += "_" + node.getElement().getOutputProduct().getName().toLowerCase().replace(" ", "_");
            }else {
                node_name = node.getElement().getOutputProduct().getName().toLowerCase().replace(" ", "_");
            }
            fileWriter.write(node_name  + " [label =\"" + node.getElement().getOutputProduct().getName() + "\"]\n");
            if(parent_name != null){
                fileWriter.write( parent_name + " -> " + node_name + " [label=\"" + node.getElement().getOutputQuantity() + "\"];\n");
            }
        }else{
            if(parent_name != null){
                node_name += "_" + node.getElement().getName().toLowerCase().replace(" ", "_");
            }else {
                node_name = node.getElement().getName().toLowerCase().replace(" ", "_");
            }
            fileWriter.write(node_name  + " [label =\"" + node.getElement().getName() + "\"]\n");
            if(parent_name != null){
                fileWriter.write( parent_name + " -> " + node_name + " [label=\"" + node.getWeight() + "\"];\n");
            }
        }
        if (node.getChildrenNodes() != null) {
            for (NAryTree.Node<ProductionNode> child : node.getChildrenNodes()) {
                writeNodesBOM(fileWriter, child, node_name);
            }
        }
    }

    private void writeNodes(FileWriter fileWriter, NAryTree.Node<ProductionNode> node, String parent_name) throws IOException {
        String node_name = parent_name;
        if(parent_name != null){
            node_name += "_" + node.getElement().getName().toLowerCase().replace(" ", "_");
        }else {
            node_name = node.getElement().getName().toLowerCase().replace(" ", "_");
        }
        if (node.getElement() instanceof Operation) {
            String output_product_node_name = node_name + "_" + node.getElement().getOutputProduct().getName().toLowerCase().replace(" ", "_");
            fileWriter.write(node_name  + " [label =\"" + node.getElement().getName() + "\",shape=box]\n");
            fileWriter.write(output_product_node_name + " [label =\"" + node.getElement().getOutputProduct().getName() + "\"]\n");
            fileWriter.write("{ rank=same; " + node_name + "; " + output_product_node_name + "; }\n");
            fileWriter.write( node_name + " -> " + output_product_node_name + " [label=\"" + node.getElement().getOutputQuantity() + "\"];\n");
        } else {
            fileWriter.write(node_name + " [label =\"" + node.getElement().getName() + "\", shape=hexagon]\n");
        }
        if (parent_name != null) {
            fileWriter.write(parent_name + " -> " + node_name + " [label=\"" + node.getWeight() + "\"];\n");
        }
        if (node.getChildrenNodes() != null) {
            for (NAryTree.Node<ProductionNode> child : node.getChildrenNodes()) {
                writeNodes(fileWriter, child, node_name);
            }
        }
    }

    private boolean pumlToPng(String pumlFilePath) {
        try {
            System.setProperty("PLANTUML_LIMIT_SIZE", "8192");
            System.setProperty("PLANTUML_DPI", "300");

            File sourceFile = new File(pumlFilePath);
            SourceFileReader reader = new SourceFileReader(sourceFile);
            List<GeneratedImage> generatedImages = reader.getGeneratedImages();
            for (GeneratedImage image : generatedImages) {
                File svgFile = image.getPngFile();
                displayDiagram(svgFile);
                svgFile.delete();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void displayDiagram(File file) {
        try {
            // Use ImageIcon to display PNG
            ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
            JLabel label = new JLabel(imageIcon);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new JScrollPane(label), BorderLayout.CENTER);

            // Set frame size to match the image size
            frame.setSize(imageIcon.getIconWidth()+50, imageIcon.getIconHeight()+50);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
