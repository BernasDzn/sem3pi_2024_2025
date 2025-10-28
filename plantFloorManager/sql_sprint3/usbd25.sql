CREATE OR REPLACE FUNCTION getProductOperationsList(
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
        )
        SELECT
            operation_id,
            operation_name
        FROM operations;

    RETURN result_set;
END;
/

-- Test 1
DECLARE
    result_cursor SYS_REFCURSOR;
    operation_id Operation.operation_id%TYPE;
    operation_name OperationType.operation_name%TYPE;
BEGIN

    SAVEPOINT svp1;

    result_cursor := getProductOperationsList('AS12945S22');

    LOOP
        FETCH result_cursor INTO operation_id, operation_name;
        EXIT WHEN result_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Operation ID: ' || operation_id ||
                             ', Operation Name: ' || operation_name);

    END LOOP;
    CLOSE result_cursor;

	ROLLBACK TO SAVEPOINT svp1;

END;
/

-- Test2
DECLARE
    result_cursor SYS_REFCURSOR;
    operation_id Operation.operation_id%TYPE;
    operation_name OperationType.operation_name%TYPE;
BEGIN

    SAVEPOINT svp1;

    result_cursor := getProductOperationsList('2gb7d8q2hdn');

    LOOP
        FETCH result_cursor INTO operation_id, operation_name;
        EXIT WHEN result_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Operation ID: ' || operation_id ||
                             ', Operation Name: ' || operation_name);

    END LOOP;
    CLOSE result_cursor;

	ROLLBACK TO SAVEPOINT svp1;

END;
/