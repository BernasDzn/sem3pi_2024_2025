package org.g102._ui.console.Register;

import org.g102._controllers.Register.RegisterProductController;
import org.g102.domain.ProductFamily;
import org.g102.tools.InputReader;

import java.util.List;

public class RegisterProductUI implements Runnable{

    RegisterProductController controller;

    public RegisterProductUI() {
        controller = new RegisterProductController();
    }

    @Override
    public void run() {

        List<ProductFamily> product_families = controller.getProductFamilies();

        if (product_families.isEmpty()) {
            System.out.println("No product families found.");
            return;
        }

        String product_id = InputReader.read("Product ID");
        String product_name = InputReader.read("Product Name");
        String product_description = InputReader.read("Product Description");
        int product_family_index = InputReader.showListReturnIndex(product_families, "Select the product family");
        controller.registerProduct(product_id, product_families.get(product_family_index).getFamilyId(), product_description, product_name);
    }
}