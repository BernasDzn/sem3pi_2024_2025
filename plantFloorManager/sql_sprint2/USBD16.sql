CREATE OR REPLACE PROCEDURE add_product(
    P_product_id IN Product.product_id%TYPE,
    P_product_name IN Product.product_name%TYPE,
    P_product_family_id IN Product.product_family_id%TYPE,
    P_description IN Product.description%TYPE
) AS

    prod_family_exists NUMBER;
    prod_already_exists NUMBER;

BEGIN

SELECT COUNT(*)
     INTO prod_already_exists
     FROM Product
     WHERE product_id =  P_product_id;

     IF prod_already_exists > 0 THEN
             DBMS_OUTPUT.PUT_LINE('Error: Product already exists.');
             RETURN;
     END IF;

SELECT COUNT(*)
     INTO prod_family_exists
     FROM ProductFamily
     WHERE product_family_name =  P_product_family_id;

     IF prod_family_exists = 0 THEN
             DBMS_OUTPUT.PUT_LINE('Error: Family does not exist.');
             RETURN;
     END IF;

INSERT INTO Product (product_id, product_name, product_family_id, description)
	VALUES (P_product_id, P_product_name, P_product_family_id, P_description);
    dbms_output.put_line('Product added successfully with ID: ' || P_product_id ||
                         ', Name: ' || P_product_name ||
                         ', Family: ' || P_product_family_id ||
                         ', Description: ' || P_description); EXCEPTION
    WHEN OTHERS THEN
dbms_output.put_line('Error: ' || SQLERRM);
END;
/