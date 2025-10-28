package org.g102.domain;

public class CustomerOrder {

    private int oid;
    private Customer customer;
    private String order_date;
    private String delivery_date;

    public CustomerOrder(int oid, Customer customer, String order_date, String delivery_date) {
        this.oid = oid;
        this.customer = customer;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
    }

    public int getOid() {return oid;}
    public int getCustomerId() {return customer.getClient_id();}
    public String getOrder_date() {return order_date;}
    public String getDelivery_date() {return delivery_date;}

    public void setOid(int oid) {this.oid = oid;}
    public void setCustomer(Customer customer) {this.customer = customer;}
    public void setOrder_date(String order_date) {this.order_date = order_date;}
    public void setDelivery_date(String delivery_date) {this.delivery_date = delivery_date;}

    public String getSQLInsert() {
        return "INSERT INTO CustomerOrder (order_id, customer_id, date_order, date_delivery) VALUES (" + oid + ", " + customer.getClient_id() + ", TO_DATE('" + order_date + "','DD/MM/YYYY'), TO_DATE('" + delivery_date + "', 'DD/MM/YYYY'));\n";
    }

    @Override
    public boolean equals(Object o){
        boolean arg1 = this.getOid() == ((CustomerOrder)o).getOid();
        return arg1;
    }

}
