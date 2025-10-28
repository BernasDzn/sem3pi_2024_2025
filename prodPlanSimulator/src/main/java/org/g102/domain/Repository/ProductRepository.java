package org.g102.domain.Repository;

import org.g102.domain.Product;

import java.util.*;

public class ProductRepository {

    private static ProductRepository instance;
    Set<Product> products;

    public ProductRepository() { products = new HashSet<Product>(); }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public boolean addProduct(Product product) {
        return products.add(product);
    }

    public void addProducts(Collection<Product> products) {
        this.products.addAll(products);
    }

    public List<String> getFinishedProductList() {
        List<String> finishedProducts = new ArrayList<String>();
        for (Product product : products) {
            if (product.isFinishedProduct()) {
                finishedProducts.add(product.getName());
            }
        }
        return finishedProducts;
    }

    public List<String> getProductsWithSeatchPrompt(String searchPrompt) {
        List<String> productsWithSearchPrompt = new ArrayList<String>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(searchPrompt.toLowerCase()) && product.isFinishedProduct()) {
                productsWithSearchPrompt.add(product.getName());
            }
        }
        if(productsWithSearchPrompt.isEmpty()){
            return null;
        }
        return productsWithSearchPrompt;
    }

    public Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

}