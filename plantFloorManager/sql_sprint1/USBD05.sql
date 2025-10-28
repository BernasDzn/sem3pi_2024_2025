SELECT
PD.product_name as "Product"
,COPList.quantity as "Quantity"
,CO.order_id as "Order Number"
,C.name as "Customer"
,CO.date_order as "Date of Order"
,CO.date_delivery as "Date of Delivery"
FROM CustomerOrder CO
    INNER JOIN CustomerOrderProductList COPList
    	ON CO.order_id = COPList.order_id
	INNER JOIN Product PD
		ON COPList.product_id = PD.product_id
    INNER JOIN Customer C
    	ON C.customer_id = CO.customer_id
WHERE date_order BETWEEN TO_DATE('10/09/2024','DD/MM/YYYY') AND TO_DATE('20/09/2024','DD/MM/YYYY')
ORDER BY CO.date_order, CO.order_id;