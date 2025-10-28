CREATE OR REPLACE PROCEDURE AddWorkstation(
	  workstation_id IN Workstation.workstation_id%TYPE 
    , workstation_type_name IN WorkstationType.type_name%TYPE 
    , workstation_name IN Workstation.name%TYPE 
) AS 
	workstation_type_id WorkstationType.type_id%TYPE; 
BEGIN 
 
    SELECT WorkstationType.type_id 
    INTO workstation_type_id 
    FROM WorkstationType 
    WHERE WorkstationType.type_name = workstation_type_name; 
 
    INSERT INTO Workstation 
    VALUES( 
    	  workstation_id 
    	, workstation_type_id 
    	, workstation_name 
    ); 
 	 
    DBMS_OUTPUT.PUT_LINE('Workstation added successfully: ' || workstation_name);  
EXCEPTION 
    WHEN OTHERS THEN 
    	DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);  
END AddWorkstation;
/