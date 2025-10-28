CREATE OR REPLACE FUNCTION get_product_parts(p_product_id IN VARCHAR2)
RETURN SYS_REFCURSOR
AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        WITH product_hierarchy(product_id, subproduct_id, quantity) AS (
            SELECT
                product_id,
                subproduct_id,
                quantity
            FROM Product_SubProducts
            WHERE product_id = p_product_id

            UNION ALL

            SELECT
                ph.product_id,
                ps.subproduct_id,
                ph.quantity * ps.quantity
            FROM Product_SubProducts ps
            JOIN product_hierarchy ph ON ps.product_id = ph.subproduct_id
        )
        SELECT
            subproduct_id AS part_id,
            SUM(quantity) AS total_quantity
        FROM product_hierarchy
        GROUP BY subproduct_id;

    RETURN result_cursor;
END;
/
DECLARE
    result_cursor SYS_REFCURSOR;
    part_id VARCHAR2(50);
    total_quantity NUMBER;
    p_product_id VARCHAR2(50) := 'AS12946S22';
    BEGIN
        result_cursor := get_product_parts(p_product_id);

        LOOP
            FETCH result_cursor INTO part_id, total_quantity;
            EXIT WHEN result_cursor%NOTFOUND;

            DBMS_OUTPUT.PUT_LINE('Part ID: ' || part_id ||
                                 ', Total Quantity: ' || total_quantity);

        END LOOP;
    CLOSE result_cursor;
END;
/
