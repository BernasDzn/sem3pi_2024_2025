package org.g102.tree;

import org.g102.domain.GenericItem;

public interface ProductionNode {

    public String getName();

    public GenericItem getOutputProduct();

    public float getOutputQuantity();

}
