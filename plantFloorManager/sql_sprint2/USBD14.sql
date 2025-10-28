CREATE OR REPLACE FUNCTION get_products_using_all_machine_types
    RETURN SYS_REFCURSOR
AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        WITH product_workstations AS (
            SELECT DISTINCT
                p.product_id,
                p.product_name,
                p.description,
                wto.workstation_type_id
            FROM Product p
                     JOIN Product_Operation po ON po.output_product_id = p.product_id
                     JOIN WorkstationType_Operation wto ON wto.operation_id = po.operation_id
        ),
             product_counts AS (
                 SELECT
                     pw.product_id,
                     pw.product_name,
                     pw.description,
                     COUNT(DISTINCT pw.workstation_type_id) as types_used,
                     (SELECT COUNT(*) FROM WorkstationType) as total_types
                 FROM product_workstations pw
                 GROUP BY pw.product_id, pw.product_name, pw.description
             )
        SELECT
            product_id,
            product_name,
            description,
            types_used,
            total_types
        FROM product_counts
        WHERE types_used = total_types
        ORDER BY product_name;

    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    v_product_id VARCHAR2(60);
    v_product_name VARCHAR2(255);
    v_description VARCHAR2(255);
    v_types_used NUMBER;
    v_total_types NUMBER;
    v_found BOOLEAN := FALSE;
BEGIN
    result_cursor := get_products_using_all_machine_types;

    DBMS_OUTPUT.PUT_LINE('Products using all machine types:');
    DBMS_OUTPUT.PUT_LINE('----------------------------------------');

    LOOP
        FETCH result_cursor INTO v_product_id, v_product_name, v_description,
            v_types_used, v_total_types;
        EXIT WHEN result_cursor%NOTFOUND;

        v_found := TRUE;
        DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_product_id);
        DBMS_OUTPUT.PUT_LINE('Name: ' || v_product_name);
        DBMS_OUTPUT.PUT_LINE('Uses ' || v_types_used || ' of ' || v_total_types || ' machine types');
        DBMS_OUTPUT.PUT_LINE('----------------------------------------');
    END LOOP;

    IF NOT v_found THEN
        DBMS_OUTPUT.PUT_LINE('No products use all machine types.');
    END IF;

    CLOSE result_cursor;
END;
/