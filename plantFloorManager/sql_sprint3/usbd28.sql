CREATE OR REPLACE FUNCTION ListReservedMaterials
RETURN SYS_REFCURSOR --return cursor declared
AS
    result_set SYS_REFCURSOR;
BEGIN
    OPEN result_set FOR --open cursor
        SELECT --get material.id, material quantity, and all suppliers
            MaterialReservation.material_id,
            SUM(MaterialReservation.quantity) AS totalQuantity,
            MaterialProcurement.supplier_id
        FROM MaterialReservation
        JOIN Material ON --when material id is the same
            MaterialReservation.material_id = Material.material_id
        JOIN MaterialProcurement ON --when material id is the same
            MaterialReservation.material_id = MaterialProcurement.material_id
        WHERE MaterialProcurement.SALE_START <= SYSDATE --get only suppliers that current date is after start date
          AND (MaterialProcurement.SALE_END >= SYSDATE OR MaterialProcurement.SALE_END IS NULL) --and only suppliers that current date is before end date or that don't have end date
        GROUP BY --grouped by unique combinations of materialReservation.material_id and materialProcurement.supplier_id
            MaterialReservation.material_id,
            MaterialProcurement.supplier_id
        ORDER BY --ordered by material
            Material.material_id;

    RETURN result_set; --return cursor
END;
/

-- testing
DECLARE
    result_cursor SYS_REFCURSOR;
    material_id Material.material_id%TYPE;
    total_quantity NUMBER;
    supplier_id MaterialProcurement.supplier_id%TYPE;
BEGIN
    SAVEPOINT point;
    INSERT INTO MaterialReservation (order_id, material_id, quantity)
VALUES (124, 'PN18324C54', 20);
    result_cursor:=ListReservedMaterials;
  LOOP
        FETCH result_cursor INTO material_id, total_quantity, supplier_id;
        EXIT WHEN result_cursor%NOTFOUND;

 DBMS_OUTPUT.PUT_LINE('Material ID: ' || material_id ||
                             ', Total Quantity: ' || total_quantity ||
                             ', Supplier ID: ' || supplier_id);
    END LOOP;
	ROLLBACK TO point;
CLOSE result_cursor;
END;/