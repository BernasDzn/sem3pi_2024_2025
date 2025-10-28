CREATE OR REPLACE PROCEDURE RegisterOrder(
    param_customer_id IN Customer.customer_id%TYPE,
    param_product_id IN CustomerOrder_Products.product_id%TYPE,
    param_quantity IN CustomerOrder_Products.quantity%TYPE,
    param_date_order IN CustomerOrder.date_order%TYPE,
    param_date_delivery IN CustomerOrder.date_delivery%TYPE,
    param_address_id IN CustomerOrder.address_id%TYPE
) IS
    var_order_id NUMBER;
    var_customer_status NUMBER;
    var_product_exists NUMBER;
BEGIN
SELECT status INTO var_customer_status
FROM Customer
WHERE customer_id = param_customer_id;

IF var_customer_status != 1 THEN
        DBMS_OUTPUT.PUT_LINE('Error: Customer is not active.');
        RETURN;
END IF;

SELECT COUNT(*)
INTO var_product_exists
FROM Product
WHERE product_id = param_product_id;

IF var_product_exists = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Error: Product does not exist.');
        RETURN;
END IF;

SELECT NVL(MAX(order_id), 0) + 1
INTO var_order_id
FROM CustomerOrder;

INSERT INTO CustomerOrder (
    order_id, customer_id, date_order, date_delivery, address_id
) VALUES (
             var_order_id, param_customer_id, param_date_order, param_date_delivery, param_address_id
         );

INSERT INTO CustomerOrder_Products (
    order_id, product_id, quantity
) VALUES (
             var_order_id, param_product_id, param_quantity
         );

DBMS_OUTPUT.PUT_LINE('Order Registered Successfully. Order ID: ' || var_order_id);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
