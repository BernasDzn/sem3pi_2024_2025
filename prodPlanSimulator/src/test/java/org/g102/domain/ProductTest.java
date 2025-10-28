package org.g102.domain;

import org.g102.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {
    Product product = new Product("1", "Chair", "A chair");

    @Test
    void getId() {
        Assertions.assertEquals("1", product.getPid());
    }

    @Test
    void getName() {
        Assertions.assertEquals("Chair", product.getName());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals("A chair", product.getDescription());
    }

    @Test
    void setId() {
        product.setPid("2");
        Assertions.assertEquals("2", product.getPid());
    }

    @Test
    void setName() {
        product.setName("Table");
        Assertions.assertEquals("Table", product.getName());
    }

    @Test
    void setDescription() {
        product.setDescription("A Table");
        Assertions.assertEquals("A Table", product.getDescription());
    }

    @Test
    void getSQLInsert() {
        Assertions.assertEquals("INSERT INTO Product (product_id, product_name, description) VALUES ('1', 'Chair', 'A chair');\n", product.getSQLInsert());
    }
}