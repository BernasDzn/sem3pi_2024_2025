package org.g102.domain;

import java.util.LinkedList;
import java.util.List;

public class ProductFamily {

    private Integer productFamilyId;
    private String name;

    public ProductFamily(Integer productFamilyId, String name, List<Product> familyProducts){
        this.productFamilyId = productFamilyId;
        this.name=name;
    }

    public ProductFamily(Integer productFamilyId, String name){
        this.productFamilyId = productFamilyId;
        this.name=name;
    }

    public Integer getFamilyId(){return productFamilyId;}

    public String getFamilyName(){return name;}

    public void setFamilyId(Integer productFamilyId){this.productFamilyId= productFamilyId;}

    public void setFamilyName(String name){this.name=name;}

    @Override
    public String toString() {
        return getFamilyName();
    }
}
