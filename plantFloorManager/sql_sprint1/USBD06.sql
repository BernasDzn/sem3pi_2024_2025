SELECT DISTINCT
CO.order_id AS "Order Number"
,WST.type_name AS "Needed Workstation Type Name"
FROM CustomerOrder CO
    JOIN CustomerOrderProductList COPL
    	ON COPL.order_id = CO.order_id
    JOIN Product PD
        ON COPL.product_id = PD.product_id
    JOIN BillOfOperations BOO
        ON PD.product_family_name = BOO.product_family_name
    JOIN WorkstationTypeOperation WSTO
        ON BOO.operation_id = WSTO.operation_id
    JOIN WorkstationType WST
        ON WST.type_id = WSTO.workstation_type_id
WHERE CO.order_id = 1;
