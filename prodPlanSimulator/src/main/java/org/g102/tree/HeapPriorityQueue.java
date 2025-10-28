/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.g102.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {
  protected ArrayList<Entry<K,V>> heap = new ArrayList<>();

  /** Creates an empty priority queue based on the natural ordering of its keys. */
  public HeapPriorityQueue() { super(); }

  /**
   * Creates an empty priority queue using the given comparator to order keys.
   * @param comp comparator defining the order of keys in the priority queue
   */
  public HeapPriorityQueue(Comparator<K> comp) { super(comp); }

    /**
     * Creates a priority queue initialized with the respective
     * key-value pairs.  The two arrays given will be paired
     * element-by-element. They are presumed to have the same
     * length. (If not, entries will be created only up to the length of
     * the shorter of the arrays)
     *
     * @param keys   an array of the initial keys for the priority queue
     * @param values an array of the initial values for the priority queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {
        if (keys == null) {
            throw new IllegalArgumentException("List of keys cannot be null");
        }
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Number of keys and values must match");
        }
        for (int j = 0; j < keys.length; j++) {
            heap.add(new PQEntry<>(keys[j], values[j]));
        }
        buildHeap();
    }

    /**
     * Creates a priority queue initialized with the respective
     * key-value pairs.  The two arrays given will be paired
     * element-by-element. They are presumed to have the same
     * length. (If not, entries will be created only up to the length of
     * the shorter of the arrays)
     *
     * @param keys   a list of the initial keys for the priority queue
     * @param values a list of the initial values for the priority queue
     */
    public HeapPriorityQueue(List<K> keys, List<V> values) {
        if (keys == null) {
            throw new IllegalArgumentException("List of keys cannot be null");
        }
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Number of keys and values must match");
        }
        for (int j = 0; j < keys.size(); j++) {
            heap.add(new PQEntry<>(keys.get(j), values.get(j)));
        }
        buildHeap();
    }

  protected int parent(int j) { return (j-1) / 2; }
  protected int left(int j) { return 2*j + 1; }
  protected int right(int j) { return 2*j + 2; }
  protected boolean hasLeft(int j) { return left(j) < heap.size(); }
  protected boolean hasRight(int j) { return right(j) < heap.size(); }

  /** Exchanges the entries at indices i and j of the array list. */
  protected void swap(int i, int j) {
    Entry<K,V> temp = heap.get(i);
    heap.set(i, heap.get(j));
    heap.set(j, temp);
  }

    /**
     * Moves the entry at index j higher, if necessary, to restore the heap property.
     */
    protected void percolateUp(int j) {
        while (j > 0) {
            int p = parent(j);
            if (compare(heap.get(j), heap.get(p)) >= 0) break;
            swap(j, p);
            j = p;
        }
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     */
    protected void percolateDown(int j) {
        while (j < heap.size()) {
            int leftIndex = left(j);
            int rightIndex = right(j);
            int smallestIndex = leftIndex;
            if (rightIndex < heap.size() && compare(heap.get(leftIndex), heap.get(rightIndex)) > 0) {
                smallestIndex = rightIndex;
            }
            if (smallestIndex >= heap.size() || compare(heap.get(j), heap.get(smallestIndex)) <= 0) {
                break;
            }
            swap(j, smallestIndex);
            j = smallestIndex;
        }
    }

    /**
     * Performs a batch bottom-up construction of the heap.
     */
    protected void buildHeap() {
        int startIndex = parent(size() - 1);
        for (int j = startIndex; j >= 0; j--) {
            percolateDown(j);
        }
    }

  @Override
  public int size() { return heap.size(); }

    @Override
    public Entry<K, V> min() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        if (key == null) {
            throw new IllegalArgumentException("Null key not permitted");
        }

        Entry<K, V> newest = new PQEntry<>(key, value);
        heap.add(newest);
        percolateUp(heap.size() - 1);
        return newest;
    }

    @Override
    public Entry<K, V> removeMin() {
        if (heap.isEmpty()) return null;
        Entry<K, V> min = heap.get(0);
        swap(0, heap.size() - 1);
        heap.remove(heap.size() - 1);
        percolateDown(0);
        return min;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        HeapPriorityQueue<K, V> list = this.clone();
        int size = list.size();

        for (int i = 0; i < size; i++) {
            Entry<K, V> entry = list.removeMin();
            sb.append(i + 1).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public String toStringCriticalPath(List<ProductionNode> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nCritical path:\n");
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i).getName());

            if(i!=0 && i!= list.size()-2){
                sb.append("\n");
                sb.append(list.get(i).getName());
            }
            if(i==list.size()-2){
                sb.append("\n");
            }
            if (i < list.size() - 2) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }


    @Override
    public HeapPriorityQueue<K, V> clone() {
        HeapPriorityQueue<K, V> newHeap = new HeapPriorityQueue<>();
        for (Entry<K, V> entry : heap) {
            newHeap.insert(entry.getKey(), entry.getValue());
        }
        return newHeap;
    }
}

