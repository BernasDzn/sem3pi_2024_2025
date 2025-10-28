package org.g102._ui.console.Register;

import org.g102._controllers.Register.DeactivateCustomerController;
import org.g102.domain.Customer;
import org.g102.tools.InputReader;

import java.util.List;

public class DeactivateCustomerUI implements Runnable{

    DeactivateCustomerController controller;

    public DeactivateCustomerUI() {
        controller = new DeactivateCustomerController();
    }

    @Override
    public void run() {
        List<Customer> customers = controller.getCustomersActive();

        if (customers.isEmpty()) {
            System.out.println("There are no active customers");
            return;
        }

        int customer_id = InputReader.showListReturnIndex(customers, "Select the customer to deactivate");
        controller.deactivateCustomer(customers.get(customer_id-1).getClient_id());
    }
}
