package org.g102.domain;

public class Component {

    private Product component;
    private int quantity;

    public Component(Product component, int quantity){
        this.component = component;
        this.quantity = quantity;
    }

    public Product getComponent(){
        return component;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setComponent(Product component){
        this.component = component;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

}
