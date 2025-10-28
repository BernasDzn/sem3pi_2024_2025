CREATE OR REPLACE PROCEDURE DeactivateCustomer(
      cid IN Customer.customer_id%TYPE
) AS rows_updated NUMBER;
BEGIN
    UPDATE Customer
    SET Customer.status = 0
    WHERE (SELECT COUNT(*) FROM CustomerOrder WHERE CustomerOrder.date_delivery > SYSDATE AND CustomerOrder.customer_id = cid) = 0 AND Customer.customer_id = cid;

    rows_updated := SQL%ROWCOUNT;

    IF rows_updated = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Customer was not deactivated: ' || cid);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Customer deactivated successfully: ' || cid);
    END IF;

EXCEPTION
    WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END DeactivateCustomer;
/