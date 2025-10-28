package org.g102.tree;

import java.util.ArrayList;
import java.util.List;

public class NAryTree<E> {

    public static class Node<E>{

        private Node<E> parent;
        private float weight;
        private E element;
        private List<Node<E>> children;

        public Node(E e, float weight){
            setElement(e);
            setWeight(weight);
            setChildrenNodes(new ArrayList<>());
        }

        public Node(E e, float weight, List<Node<E>> children){
            setElement(e);
            setWeight(weight);
            setChildrenNodes(children);
        }

        public Node<E> getParent() {return parent;}
        public float getWeight() {return weight;}
        public E getElement() {return element;}
        public List<Node<E>> getChildrenNodes() {return children;}

        public void setParent(Node<E> parent) {this.parent = parent;}
        public void setElement(E element) {this.element = element;}
        public void setWeight(float weight) {this.weight = weight;}
        public void setChildrenNodes(List<Node<E>> childrenNodes) {
            this.children = childrenNodes;
            for (Node<E> child : children)
                child.setParent(this);
        }

        public void addChild(Node<E> childNode){
            childNode.setParent(this);
            getChildrenNodes().add(childNode);
        }

        public void removeChild(Node<E> childNode){
            getChildrenNodes().remove(childNode);
        }

        public String toString(int level) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++){
                if(i == level-1){
                    sb.append("└── ");
                }else {
                    sb.append("     ");
                }
            }
            if(getWeight() != 0){
                sb.append("x").append(getWeight()).append(" ");
            }
            sb.append(element.toString());
            sb.append("\n");
            if(getChildrenNodes() != null) {
                for (Node<E> n : getChildrenNodes()) {
                    sb.append(n.toString(level + 1));
                }
            }
            return sb.toString();
        }
    }

    private Node<E> root;

    public Node<E> getRoot() {
        return root;
    }

    public void setRoot(Node<E> root) {
        this.root = root;
    }

    public NAryTree(){
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean exists(E element) {
        return find(root,element);
    }

    public int size() {
        return getNumberOfDescendants(root) + 1;
    }

    public int getNumberOfDescendants(Node<E> node){
        int n = node.getChildrenNodes().size();
        for (Node<E> child : node.getChildrenNodes())
            n = getNumberOfDescendants(child);
        return n;
    }

    protected boolean find(Node<E> node,E element){
        if(node == null) return false;
        if(node.getElement().equals(element)) return true;
        for(Node<E> n : node.getChildrenNodes())
            return find(n,element);
        return false;
    }

    public Node<E> findNode(Node<E> node,E element){
        if(node == null) return null;
        if(node.getElement().equals(element)) return node;
        for(Node<E> n : node.getChildrenNodes()) {
            Node<E> result = findNode(n, element);
            if (result != null)
                return result;
        }
        return null;
    }

    private ArrayList<Node<E>> clone(ArrayList<Node<E>> list) {
        return new ArrayList<>(list);
    }

    private void getPath(Node<E> node, ArrayList<Node<E>> currentPath, ArrayList<ArrayList<Node<E>>> paths) {
        if (currentPath == null)
            return;

        currentPath.add(node);

        if (node.getChildrenNodes().isEmpty()) {
            // This is a leaf
            paths.add(clone(currentPath));
        }
        for (Node<E> child : node.getChildrenNodes())
            getPath(child, currentPath, paths);

        int index = currentPath.indexOf(node);
        for (int i = index; i < currentPath.size(); i++)
            currentPath.remove(index);
    }

    public ArrayList<ArrayList<Node<E>>> getPathsFromRootToAnyLeaf() {
        ArrayList<ArrayList<Node<E>>> paths = new ArrayList<ArrayList<Node<E>>>();
        ArrayList<Node<E>> currentPath = new ArrayList<Node<E>>();
        getPath(root, currentPath, paths);

        return paths;
    }

    public ArrayList<Node<E>> getLongestPathFromRootToAnyLeaf() {
        ArrayList<Node<E>> longestPath = null;
        int max = 0;
        for (ArrayList<Node<E>> path : getPathsFromRootToAnyLeaf()) {
            if (path.size() > max) {
                max = path.size();
                longestPath = path;
            }
        }
        return longestPath;
    }

    public String toString(){
        if(isEmpty()) return "Tree is empty.";
        return root.toString(0);
    }


    public List<E> findNodesOfElement(Class clazz){
        List<E> elements = new ArrayList<>();
        findNodesOfElement(root, clazz, elements);
        return elements;
    }

    private void findNodesOfElement(Node<E> node, Class<E> clazz, List<E> elements){
        if(node == null) return;
        if(clazz.isInstance(node.getElement())){
            elements.add((E) node.getElement());
        }
        for(Node<E> n : node.getChildrenNodes())
            findNodesOfElement(n, clazz, elements);
    }

    public float getCost() {
        return getCost(root);
    }

    private float getCost(Node<E> node) {
        float cost = 0;
        for (Node<E> n : node.getChildrenNodes())
            cost += n.getWeight() * getCost(n);
        return cost;
    }

    public int getDepth(Node<E> node) {
        if (node == null)
            return 0;
        int depth = 0;
        if(node.getParent() != null)
            return getDepth(node.getParent()) + 1;
        return depth;
    }
}
