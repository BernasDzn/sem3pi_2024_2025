CREATE OR REPLACE FUNCTION checkIfStockAvailable(
    order_id_in IN CustomerOrder.order_id%TYPE
) RETURN BOOLEAN IS
    v_available BOOLEAN := TRUE;
    v_materials_cursor SYS_REFCURSOR;
    v_material_id Material.material_id%TYPE;
    v_total_material_quantity NUMBER;
    v_available_stock NUMBER;
    v_order_count NUMBER;

    CURSOR get_order_products(p_order_id CustomerOrder.order_id%TYPE) IS
        SELECT
            OPL.product_id,
            OPL.quantity AS order_quantity
        FROM OrderProductList OPL
        WHERE OPL.order_id = p_order_id;

BEGIN
    SELECT COUNT(*) INTO v_order_count FROM CustomerOrder WHERE order_id = order_id_in;
    IF v_order_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20000, 'Order does not exist.');
    END IF;

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

    RETURN v_available;
EXCEPTION
    WHEN OTHERS THEN
        IF v_materials_cursor%ISOPEN THEN
            CLOSE v_materials_cursor;
        END IF;
        RETURN v_available;
END;
/

DECLARE
    v_result BOOLEAN;
    v_result2 BOOLEAN;
BEGIN
    v_result := checkIfStockAvailable(123);
    v_result2 := checkIfStockAvailable(125);
    DBMS_OUTPUT.PUT_LINE('UNIT TEST 1');
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Test passed.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test failed.');
    END IF;

    DBMS_OUTPUT.PUT_LINE('UNIT TEST 2');
    IF v_result2 THEN
        DBMS_OUTPUT.PUT_LINE('Test failed.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test passed.');
    END IF;
END;
