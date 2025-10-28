CREATE OR REPLACE TRIGGER check_min_execution_time
BEFORE INSERT OR UPDATE OF expected_execution_time
ON Operation
FOR EACH ROW
DECLARE
    highest_max_ex_time WorkstationType_Operation.maximum_execution_time%TYPE;
BEGIN
    SELECT MAX(maximum_execution_time) INTO highest_max_ex_time
    FROM WorkstationType_Operation
    WHERE op_type_id = :NEW.op_type_id;
    IF :NEW.expected_execution_time > highest_max_ex_time
    THEN
        RAISE_APPLICATION_ERROR(-20001, 'Min execution time must be less than the maximum execution time of every workstation type where it may be run');
    END IF;
END;
/