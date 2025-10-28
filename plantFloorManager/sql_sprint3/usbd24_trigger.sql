CREATE OR REPLACE TRIGGER check_output_in_input
BEFORE INSERT
ON Operation_InputList
FOR EACH ROW
DECLARE
    final_product_id FinalProduct.final_product_id%TYPE;
BEGIN
    SELECT final_product_id INTO final_product_id
    FROM Operation WHERE operation_id = :NEW.operation_id;
    IF :NEW.input_id = final_product_id
    THEN
        RAISE_APPLICATION_ERROR(-20001, 'Output product cannot be an input in its own BOO');
    END IF;
END;
/