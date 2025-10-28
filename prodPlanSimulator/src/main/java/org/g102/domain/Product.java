package org.g102.domain;

import java.util.LinkedList;
import java.util.List;

public class Product extends GenericItem{

    private String description;
    private List<Component> components;
    private boolean isFinishedProduct;
    private ProductFamily family;

    public Product(String pid, String name, String description){
        super(pid, name);
        this.description = description;
        components = new LinkedList<>();
        isFinishedProduct = false;
    }

    public Product(String pid, String name, String description, ProductFamily family){
        super(pid, name);
        this.description = description;
        components = new LinkedList<>();
        isFinishedProduct = false;
        this.family= family;
    }

    public Product(String pid, String name, String description, boolean isFinishedProduct){
        super(pid, name);
        this.description = description;
        components = new LinkedList<>();
        this.isFinishedProduct = isFinishedProduct;
    }

    public String getDescription(){return description;}
    public List<Component> getComponents(){return components;}
    public String getFamilyName(){return family.getFamilyName();}
    public boolean isFinishedProduct(){return isFinishedProduct;}

    public void setDescription(String description){this.description = description;}
    public void setComponents(List<Component> components){this.components = components;}
    public void setFinishedProduct(boolean isFinishedProduct){this.isFinishedProduct = isFinishedProduct;}

    public String getSQLInsert(String productFamily){
        return "INSERT INTO Product (product_id, product_name, product_family_name, description) VALUES ('" + getPid() + "', '" + getName() + "', '" + productFamily + "', '" + description + "');\n";
    }

    public String getSQLInsert(){
        return "INSERT INTO Product (product_id, product_name, description) VALUES ('" + getPid() + "', '" + getName() + "', '" + description + "');\n";
    }

    @Override
    public String toString(){
        return super.getName();
    }


}
