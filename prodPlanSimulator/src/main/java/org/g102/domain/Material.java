package org.g102.domain;

public class Material extends GenericItem {

    public Material(String mid, String name){
        super(mid, name);
    }

    public String getSQLInsert(){
        return "INSERT INTO Material VALUES ('" + getPid() + "', '" + getName() + "');";
    }

    @Override
    public String toString(){
        return getName() + " (mid: " + getPid() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            System.out.println("same object");
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            System.out.println("null or different class");
            return false;
        }
        Material m = (Material) obj;
        System.out.println("comparing " + getPid() + " with " + m.getPid());
        return getPid().equals(m.getPid());
    }

    @Override
    public int hashCode() {
        return getPid().hashCode();
    }

}
