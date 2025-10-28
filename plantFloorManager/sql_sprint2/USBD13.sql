CREATE OR REPLACE FUNCTION GetProductOperations(product_id_input VARCHAR2)
RETURN SYS_REFCURSOR
IS
    product_operations_cursor SYS_REFCURSOR;
BEGIN
OPEN product_operations_cursor FOR
    WITH SubProducts AS (
        SELECT
            product_id AS parent_product_id,
            subproduct_id
        FROM
            Product_SubProducts
        START WITH product_id = product_id_input
        CONNECT BY PRIOR subproduct_id = product_id
    ),
    AllProducts AS (
        SELECT
            DISTINCT subproduct_id AS product_id
        FROM
            SubProducts
        UNION
        SELECT
            product_id_input AS product_id
        FROM
            DUAL
    ),
    MaterialInputs AS (
        SELECT
            Product_Operation.operation_id,
            Product_Operation.output_product_id AS product_id,
            Product_Operation.quantity AS output_quantity,
            Product_Materials.material_id AS input_material_id,
            Product_Materials.quantity AS input_quantity
        FROM
            Product_Operation
        LEFT JOIN
            Product_Materials
        ON
            Product_Operation.output_product_id = Product_Materials.product_id
        WHERE
            Product_Operation.output_product_id IN (SELECT product_id FROM AllProducts)
    ),
    SubProductInputs AS (
        SELECT
            Product_Operation.operation_id,
            Product_Operation.output_product_id AS product_id,
            Product_Operation.quantity AS output_quantity,
            Product_SubProducts.subproduct_id AS input_subproduct_id
        FROM
            Product_Operation
        LEFT JOIN
            Product_SubProducts
        ON
            Product_Operation.output_product_id = Product_SubProducts.product_id
        WHERE
            Product_Operation.output_product_id IN (SELECT product_id FROM AllProducts)
    ),
    CombinedInputs AS (
        SELECT
            MaterialInputs.operation_id,
            MaterialInputs.product_id,
            MaterialInputs.output_quantity,
            MaterialInputs.input_material_id,
            MaterialInputs.input_quantity,
            SubProductInputs.input_subproduct_id
        FROM
            MaterialInputs
        FULL OUTER JOIN
            SubProductInputs
        ON
            MaterialInputs.operation_id = SubProductInputs.operation_id
            AND MaterialInputs.product_id = SubProductInputs.product_id
    )
SELECT
    Operation.operation_id,
    Operation.description AS operation_description,
    WorkstationType.type_id AS workstation_type,
    CombinedInputs.product_id,
    CombinedInputs.output_quantity,
    CombinedInputs.input_material_id,
    CombinedInputs.input_quantity,
    CombinedInputs.input_subproduct_id
FROM
    CombinedInputs
        INNER JOIN
    Operation
    ON
            CombinedInputs.operation_id = Operation.operation_id
        INNER JOIN
    WorkstationType_Operation
    ON
            Operation.operation_id = WorkstationType_Operation.operation_id
        INNER JOIN
    WorkstationType
    ON
            WorkstationType_Operation.workstation_type_id = WorkstationType.type_id;

RETURN product_operations_cursor;
END;
/
DECLARE
product_ops SYS_REFCURSOR;
    operation_id NUMBER;
    operation_description VARCHAR2(255);
    workstation_type VARCHAR2(255);
    product_id VARCHAR2(60);
	input_subproduct_id VARCHAR(255);
	input_material_id VARCHAR(255);
	input_quantity NUMBER;
	output_quantity NUMBER;
BEGIN
    product_ops := GetProductOperations('IP12945A02');

    LOOP
FETCH product_ops INTO operation_id, operation_description, workstation_type,
        						product_id, output_quantity, input_material_id, input_quantity, input_subproduct_id;
        EXIT WHEN product_ops%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(
            'Operation ID: ' || operation_id ||
            ', Description: ' || operation_description ||
            ', Workstation Type: ' || workstation_type ||
            ', Output ID: ' || product_id ||
            ', Output Quantity: ' || output_quantity ||
            ', Input Material ID: ' || input_material_id ||
            ', Input Quantity: ' || input_quantity ||
            ', Input SubProduct: ' || input_subproduct_id
        );
END LOOP;

CLOSE product_ops;
END;