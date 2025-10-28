package org.g102.domain;

public class OrderProductList {

    private CustomerOrder customerOrder;
    private Product product;
    private int quantity;

    public OrderProductList(CustomerOrder customerOrder, Product product, int quantity) {
        this.customerOrder = customerOrder;
        this.product = product;
        this.quantity = quantity;
    }

    public CustomerOrder getCustomerOrder() {return customerOrder;}
    public Product getProduct() {return product;}
    public int getQuantity() {return quantity;}

    public void setCustomerOrder(CustomerOrder customerOrder) {this.customerOrder = customerOrder;}
    public void setProduct(Product product) {this.product = product;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getSQLInsert() {
        return "INSERT INTO CustomerOrderProductList (order_id, product_id, quantity) VALUES (" + customerOrder.getOid() + ", '" + product.getPid() + "', " + quantity + ");\n";
    }

}
