package org.g102._ui.console.Register;

import org.g102._controllers.Register.RegisterOrderController;
import org.g102.domain.Address;
import org.g102.domain.Customer;
import org.g102.domain.Product;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterOrderUI implements Runnable{

    RegisterOrderController controller;

    public RegisterOrderUI() {
        controller = new RegisterOrderController();
    }

    @Override
    public void run() {

        List<Customer> customers = controller.getCustomers();
        List<Address> addresses = controller.getAddresses();
        List<Product> products = controller.getProducts();

        if(customers.isEmpty() || addresses.isEmpty() || products.isEmpty()){
            ConsoleWriter.displayError("There are no customers, addresses or products registered");
            return;
        }

        int order_id = Integer.parseInt(InputReader.read("Order ID"));
        int customer_index = InputReader.showListReturnIndex(customers, "Select the customer");
        int address_index = InputReader.showListReturnIndex(addresses, "Select the address");
        System.out.println("Order date:");
        int order_date_day = Integer.parseInt(InputReader.read("   Day"));
        int order_date_month = Integer.parseInt(InputReader.read("   Month"));
        int order_date_year = Integer.parseInt(InputReader.read("   Year"));
        String order_date = order_date_day + "/" + order_date_month + "/" + order_date_year;
        System.out.println("Delivery date:");
        int delivery_date_day = Integer.parseInt(InputReader.read("   Day"));
        int delivery_date_month = Integer.parseInt(InputReader.read("   Month"));
        int delivery_date_year = Integer.parseInt(InputReader.read("   Year"));
        String delivery_date = delivery_date_day + "/" + delivery_date_month + "/" + delivery_date_year;
        int product_index = 0;
        HashMap<Product, Integer> products_list = new HashMap<>();
        products.addFirst(new Product("0", "Finish", "Finish"));
        while (product_index != 1 && !products.isEmpty()){
            product_index = InputReader.showListReturnIndex(products, "Select Products to add to the order");
            if(product_index != 1){
                int quantity = Integer.parseInt(InputReader.read("Quantity"));
                products_list.put(products.get(product_index-1), quantity);
                products.remove(product_index-1);
            }
            if (products.isEmpty()){
                ConsoleWriter.displayError("No more products to add");
                product_index = 1;
            }
        }
        controller.registerOrder(order_id, customers.get(customer_index - 1).getClient_id(), addresses.get(address_index - 1).getAddress_id(), order_date, delivery_date, products_list);
    }

}
