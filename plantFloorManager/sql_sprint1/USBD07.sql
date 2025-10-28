SELECT
    product_name AS "Missing Components/Materials",
    SUM(COPL.quantity * BOM.quantity) AS "Quantity To Order"
FROM Product PD
    INNER JOIN CustomerOrderProductList COPL
    	ON COPL.order_id = 1
	INNER JOIN BillOfMaterials BOM
    	ON (BOM.component_id = PD.product_id AND BOM.product_id = COPL.product_id)
GROUP BY product_name;