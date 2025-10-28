package org.g102.domain;

import org.g102.tree.ProductionNode;

public class GenericItem implements ProductionNode, Comparable<GenericItem> {

    private String pid;
    private String name;

    public GenericItem(String pid, String name){
        this.pid = pid;
        this.name = name;
    }

    public String getPid(){return pid;}
    public String getName(){return name;}

    @Override
    public GenericItem getOutputProduct() {
        return null;
    }

    @Override
    public float getOutputQuantity() {
        return 0;
    }

    public void setPid(String pid){this.pid = pid;}
    public void setName(String name){this.name = name;}

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GenericItem)
            return this.getPid().equals(((GenericItem) obj).getPid());
        return false;
    }


    @Override
    public int compareTo(GenericItem o) {
        return o.getPid().compareTo(o.pid);
    }

    @Override
    public int hashCode() {
        return pid.hashCode();
    }
}
