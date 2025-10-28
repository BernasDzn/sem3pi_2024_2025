CREATE OR REPLACE PROCEDURE reserve_materials_for_order(
    p_order_id IN CustomerOrder.order_id%TYPE,
    p_success OUT BOOLEAN
) IS
    v_can_fulfill BOOLEAN;
    material_id Material.material_id%TYPE;
    total_material_quantity Operation_InputList.quantity%TYPE;
    material_cursor SYS_REFCURSOR;

    TYPE t_material_totals IS TABLE OF NUMBER
        INDEX BY VARCHAR2(15);
    v_material_totals t_material_totals;
BEGIN
    p_success := false;
    v_can_fulfill := checkIfStockAvailable(p_order_id);

    IF v_can_fulfill = true THEN

        FOR order_product IN (SELECT product_id, quantity
                              FROM OrderProductList
                              WHERE order_id = p_order_id)
            LOOP
                material_cursor := getProductMaterialList(order_product.product_id);
                LOOP
                    FETCH material_cursor INTO material_id, total_material_quantity;
                    EXIT WHEN material_cursor%NOTFOUND;

                    IF v_material_totals.EXISTS(material_id) THEN
                        v_material_totals(material_id) := v_material_totals(material_id) + (total_material_quantity * order_product.quantity);
                    ELSE
                        v_material_totals(material_id) := total_material_quantity * order_product.quantity;
                    END IF;
                END LOOP;
                CLOSE material_cursor;
            END LOOP;

        DELETE FROM MaterialReservation
        WHERE order_id = p_order_id;

        material_id := v_material_totals.FIRST;
        WHILE material_id IS NOT NULL LOOP
                INSERT INTO MaterialReservation (order_id, material_id, quantity) VALUES (p_order_id, material_id, v_material_totals(material_id));
                material_id := v_material_totals.NEXT(material_id);
            END LOOP;

        p_success := true;
        COMMIT;
    ELSE
        ROLLBACK;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        p_success := false;
        RAISE;
END reserve_materials_for_order;
/
DECLARE
    v_success BOOLEAN;
    v_success2 BOOLEAN;
BEGIN
    reserve_materials_for_order(123, v_success);
    reserve_materials_for_order(125, v_success2);
    DBMS_OUTPUT.PUT_LINE('UNIT TEST 1');
    IF v_success THEN
        DBMS_OUTPUT.PUT_LINE('Test passed.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test failed.');
    END IF;

    DBMS_OUTPUT.PUT_LINE('UNIT TEST 2');
    IF v_success2 THEN
        DBMS_OUTPUT.PUT_LINE('Test failed.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test passed.');
    END IF;
END;
/