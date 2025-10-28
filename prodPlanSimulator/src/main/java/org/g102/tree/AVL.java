package org.g102.tree;

import java.util.List;

/**
 * @param <E>
 */
public class AVL<E extends Comparable<E>> extends BST<E> {

    private int balanceFactor(Node<E> node) {
        return height(node.getLeft()) - height(node.getRight());
    }

    private Node<E> rightRotation(Node<E> node) {
        Node<E> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());
        leftChild.setRight(node);
        return leftChild;
    }

    private Node<E> leftRotation(Node<E> node) {
        Node<E> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());
        rightChild.setLeft(node);
        return rightChild;
    }

    private Node<E> twoRotations(Node<E> node) {
        if (balanceFactor(node) < 0) {
            node.setLeft(leftRotation(node.getLeft()));
            return rightRotation(node);
        } else {
            node.setRight(rightRotation(node.getRight()));
            return leftRotation(node);
        }
    }

    private Node<E> balanceNode(Node<E> node) {
        int balance = balanceFactor(node);
        if (balance > 1) {
            if (balanceFactor(node.getLeft()) < 0) {
                node.setLeft(leftRotation(node.getLeft()));
            }
            return rightRotation(node);
        } else if (balance < -1) {
            if (balanceFactor(node.getRight()) > 0) {
                node.setRight(rightRotation(node.getRight()));
            }
            return leftRotation(node);
        }
        return node;
    }

    @Override
    public void insert(E element) {
        root = insert(element, root);
    }

    private Node<E> insert(E element, Node<E> node) {
        if (node == null) {
            return new Node<>(element, null, null);
        }
        if (element.compareTo(node.getElement()) < 0) {
            node.setLeft(insert(element, node.getLeft()));
        } else if (element.compareTo(node.getElement()) > 0) {
            node.setRight(insert(element, node.getRight()));
        }
        return balanceNode(node);
    }

    @Override
    public void remove(E element) {
        root = remove(element, root());
    }

    private Node<E> remove(E element, BST.Node<E> node) {
        if (node == null) {
            return null;
        }
        if (element.compareTo(node.getElement()) < 0) {
            node.setLeft(remove(element, node.getLeft()));
        } else if (element.compareTo(node.getElement()) > 0) {
            node.setRight(remove(element, node.getRight()));
        } else {
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else {
                Node<E> minNode = min(node.getRight());
                node.setElement(minNode.getElement());
                node.setRight(remove(minNode.getElement(), node.getRight()));
            }
        }
        return balanceNode(node);
    }

    private Node<E> min(Node<E> node) {
        if (node.getLeft() == null) {
            return node;
        } else {
            return min(node.getLeft());
        }
    }

    public List<Node<E>> getAllNodes(){
        return super.getAllNodes();
    }

    public boolean equals(Object otherObj) {
        if (this == otherObj)
            return true;
        if (otherObj == null || this.getClass() != otherObj.getClass())
            return false;
        AVL<E> second = (AVL<E>) otherObj;
        return equals(root, second.root);
    }

    public boolean equals(Node<E> root1, Node<E> root2) {
        if (root1 == null && root2 == null)
            return true;
        else if (root1 != null && root2 != null) {
            if (root1.getElement().compareTo(root2.getElement()) == 0) {
                return equals(root1.getLeft(), root2.getLeft())
                        && equals(root1.getRight(), root2.getRight());
            } else
                return false;
        } else return false;
    }
}