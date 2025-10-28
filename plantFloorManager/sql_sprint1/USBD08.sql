SELECT
OP.description as "Operation the factory supports"
FROM Operation OP
	INNER JOIN WorkstationTypeOperation WTO
		ON WTO.operation_id = OP.operation_id
	INNER JOIN WorkstationType WTT
		ON WTO.workstation_type_id = WTT.type_id
	INNER JOIN Workstation WT
		ON WTT.type_id = WT.workstation_type_id
GROUP BY OP.description;