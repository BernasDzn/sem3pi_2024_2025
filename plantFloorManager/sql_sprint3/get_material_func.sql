CREATE OR REPLACE FUNCTION getProductMaterialList(
    final_product_id_in IN FinalProduct.final_product_id%TYPE
)
RETURN SYS_REFCURSOR
    AS result_set SYS_REFCURSOR;
BEGIN
    OPEN result_set FOR
        WITH operations(operation_id, operation_name) AS (
            SELECT
                Operation.operation_id,
                OperationType.operation_name
            FROM Operation
            INNER JOIN OperationType ON
                OperationType.op_type_id = Operation.op_type_id
            WHERE final_product_id = final_product_id_in

            UNION ALL

            SELECT
                Operation.operation_id,
                OperationType.operation_name
            FROM Operation_InputList
            INNER JOIN operations ON
                operations.operation_id = Operation_InputList.operation_id
            INNER JOIN Operation ON
                Operation_InputList.operation_id = operations.operation_id
            INNER JOIN OperationType ON
                OperationType.op_type_id = Operation.op_type_id
            WHERE Operation.final_product_id = Operation_InputList.input_id
        ),
        materials(material_id, total_material_quantity) AS (
            SELECT
                Operation_InputList.input_id AS material_id,
                SUM(Operation_InputList.quantity) AS total_material_quantity
            FROM Operation_InputList
            JOIN Material ON
                Material.material_id = Operation_InputList.input_id
            WHERE Operation_InputList.operation_id IN (
                SELECT
                    operation_id
                FROM operations
            )
            GROUP BY Operation_InputList.input_id
        )
        SELECT
            materials.material_id,
            materials.total_material_quantity
        FROM materials;

    RETURN result_set;
END;
/

-- Example of how to use the function
DECLARE
    result_cursor SYS_REFCURSOR;
    material_id Material.material_id%TYPE;
    total_material_quantity Operation_InputList.quantity%TYPE;
BEGIN
    result_cursor := getProductMaterialList('AS12945S22');

    LOOP
        FETCH result_cursor INTO material_id, total_material_quantity;
        EXIT WHEN result_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Material ID: ' || material_id ||
                             ', Total Material Quantity: ' || total_material_quantity);

    END LOOP;
    CLOSE result_cursor;
END;
/