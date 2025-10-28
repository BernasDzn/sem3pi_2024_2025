package org.g102.tree;

import org.g102.domain.GenericItem;

import java.util.ArrayList;

public class QuantityNode<E extends Comparable<E>> implements Comparable<QuantityNode<GenericItem>> {
    private Float quantity;
    private ArrayList<E> materials;

    public QuantityNode(Float q, ArrayList<E> quantityMaterials) {
        quantity= q;
        materials= quantityMaterials;
    }



    public Float getQuantity() {return quantity;
    }
    public ArrayList<E> getMaterials(){return materials;}

    @Override
    public int compareTo(QuantityNode<GenericItem> o) {
        return o.getQuantity().compareTo(quantity);
    }




}