# Time Complexity Analysis

## USBD25

---

## USBD26

The time complexity of this US is O(p×m) where P is the number of products in a given order and M is the number
of materials in a product's BOO.

This US uses an externally defined function getProductMaterialList() that returns the list of materials for a given product.
This function is called for each product in the order, with a time complexity of O(m), where M is the number of materials in the product's BOO.

We initially define a cursor that stores the list of products for a given order.
```SQL
CURSOR get_order_products(p_order_id CustomerOrder.order_id%TYPE) IS
    SELECT
        OPL.product_id,
        OPL.quantity AS order_quantity
    FROM OrderProductList OPL
    WHERE OPL.order_id = p_order_id;
```

The main loop runs for every product in the previously defined cursor, and inside that loop we call the getProductMaterialList() function.
This sums up to a time complexity of O(p×m).

After that, we iterate over the materials of the product and check if the available stock is enough for the order.
This part of the code has a time complexity of O(m) as we iterate over the materials of the product.

We can so conclude that the time complexity of this US is O(p×2m), that can be simplified to O(p×m).

```SQL
FOR product_rec IN get_order_products(order_id_in) LOOP

    v_materials_cursor := getProductMaterialList(product_rec.product_id);

    LOOP
        FETCH v_materials_cursor INTO v_material_id, v_total_material_quantity;
        EXIT WHEN v_materials_cursor%NOTFOUND;

        SELECT COALESCE(MS.qt_in_stock, 0) - COALESCE(SUM(MR.quantity), 0)
        INTO v_available_stock
        FROM MaterialStock MS
                 LEFT JOIN MaterialReservation MR ON MR.material_id = MS.material_id
        WHERE MS.material_id = v_material_id
        GROUP BY MS.qt_in_stock;

        IF v_available_stock < (v_total_material_quantity * product_rec.order_quantity) THEN
            v_available := FALSE;
            EXIT;
        END IF;
    END LOOP;

    CLOSE v_materials_cursor;

    IF NOT v_available THEN
        EXIT;
    END IF;
END LOOP;
```

---
## USBD27

The time complexity of this procedure is O(p×m), where:

p is the number of products in the order\
m is the number of materials in a product's bill of materials

This US uses an externally defined function getProductMaterialList() that returns the list of materials for a given product.\
This function is called for each product in the order, with a time complexity of O(m).

We initially query the list of products for a given order:
```sql
FOR order_product IN (SELECT product_id, quantity 
                    FROM OrderProductList 
                    WHERE order_id = p_order_id)
```
Time Complexity: O(p)\
This query retrieves all products for a given order.
### Stock Availability Check:
```sql
v_can_fulfill := checkIfStockAvailable(p_order_id);
```
Time Complexity: O(p×m)\
This function checks each material (m) for each product (p)\
Function is called once per procedure execution\
Before inserting new reservations, we delete any previous reservations for the order:
```sql
DELETE FROM MaterialReservation
WHERE order_id = p_order_id;
```
Time Complexity: O(r), where r is the number of existing reservations\
Single operation with indexed access on order_id
### Main Loops:
First, we calculate the total quantities needed for each material (EXISTS and assignment operations are O(1)):

```sql
FOR order_product IN (SELECT ...) LOOP
    material_cursor := getProductMaterialList(order_product.product_id);
    LOOP
        FETCH material_cursor INTO material_id, total_material_quantity;
        EXIT WHEN material_cursor%NOTFOUND;
        
        IF v_material_totals.EXISTS(material_id) THEN
            v_material_totals(material_id) := v_material_totals(material_id) + 
                (total_material_quantity * order_product.quantity);
        ELSE
            v_material_totals(material_id) := total_material_quantity * order_product.quantity;
        END IF;
    END LOOP;
END LOOP;
```

Outer Loop: O(p) iterations over products\
Inner Loop: O(m) iterations over materials per product\
getProductMaterialList(): O(m) for each call\
Hashmap operations: O(1)\
Combined complexity: O(p×m)\
Then, we insert the aggregated quantities:
```sql
material_id := v_material_totals.FIRST;
WHILE material_id IS NOT NULL LOOP
    INSERT INTO MaterialReservation (...) VALUES (...);
    material_id := v_material_totals.NEXT(material_id);
END LOOP;
```
Time Complexity: O(m)\
Single loop over unique materials with O(1) operations per iteration\
Total Complexity Calculation\
Initial product query: O(p)\
Stock check: O(p×m)\
Delete operation: O(r)\
First calculation loop: O(p×m)\
Final insertion loop: O(m)\
The dominant term is O(p×m) which occurs in both the stock check and the first calculation loop. All other operations have lesser complexity,
so they don't affect the final Big O notation.

---