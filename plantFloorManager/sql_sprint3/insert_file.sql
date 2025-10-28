-- Customer Types
INSERT INTO CustomerType (type_id, type) VALUES (1, 'Individual');
INSERT INTO CustomerType (type_id, type) VALUES (2, 'Company');
-- Addresses
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (1, 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal');
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (2, 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal');
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (3, 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal');
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (4, 'Křemencova 11', '110 00', 'Nové Město', 'Czechia');
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (5, 'Av. da Boavista 123', '4100-111', 'Porto', 'Portugal');
INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES (6, 'Av. da República 123', '1050-111', 'Lisboa', 'Portugal');
-- Customers
INSERT INTO Customer (customer_id, name, vatin, address_id, type_id, email_address, phone_number, status) VALUES (456, 'Carvalho & Carvalho, Lda', 'PT501245987', 1, 2, 'idont@care.com', '003518340500', 1);
INSERT INTO Customer (customer_id, name, vatin, address_id, type_id, email_address, phone_number, status) VALUES (785, 'Tudo para a casa, Lda', 'PT501245488', 2, 2, 'me@neither.com', '003518340500', 1);
INSERT INTO Customer (customer_id, name, vatin, address_id, type_id, email_address, phone_number, status) VALUES (657, 'Sair de Cena', 'PT501242417', 3, 2, 'some@email.com', '003518340500', 1);
INSERT INTO Customer (customer_id, name, vatin, address_id, type_id, email_address, phone_number, status) VALUES (348, 'U Fleku', 'CZ6451237810', 4, 2, 'some.random@email.cz', '004201234567', 1);
-- Product Families
INSERT INTO ProductFamily (family_id, family_name) VALUES (125, 'Pro Line pots');
INSERT INTO ProductFamily (family_id, family_name) VALUES (130, 'La Belle pots');
INSERT INTO ProductFamily (family_id, family_name) VALUES (132, 'Pro Line pans');
INSERT INTO ProductFamily (family_id, family_name) VALUES (145, 'Pro Line lids');
INSERT INTO ProductFamily (family_id, family_name) VALUES (146, 'Pro Clear lids');
-- Suppliers
INSERT INTO Supplier (supplier_id, supplier_name, supplier_address_id) VALUES (12345, 'Ferro e Metais do Norte Lda.', 5);
INSERT INTO Supplier (supplier_id, supplier_name, supplier_address_id) VALUES (12298, 'Aços e Materiais Gerais S.A.', 6);
-- Workstation Types
INSERT INTO WorkstationType (type_id, type_name) VALUES ('A4578', '600t cold forging stamping press');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('A4588', '600t cold forging precision stamping press');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('A4598', '1000t cold forging precision stamping press');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('S3271', 'Handle rivet');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('K3675', 'Packaging');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('K3676', 'Packaging for large itens');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('C5637', 'Border trimming');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('D9123', 'Spot welding');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('Q5478', 'Teflon application station');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('Q3547', 'Stainless steel polishing');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('T3452', 'Assembly T1');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('G9273', 'Circular glass cutting');
INSERT INTO WorkstationType (type_id, type_name) VALUES ('G9274', 'Glass trimming');
-- Workstations
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (9875, 'A4578', 'Press 01', '220-630t cold forging press');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (9886, 'A4578', 'Press 02', '220-630t cold forging press');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (9847, 'A4588', 'Press 03', '220-630t precision cold forging press');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (9855, 'A4588', 'Press 04', '160-1000t precison cold forging press');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (8541, 'S3271', 'Rivet 02', 'Rivet station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (8543, 'S3271', 'Rivet 03', 'Rivet station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (6814, 'K3675', 'Packaging 01', 'Packaging station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (6815, 'K3675', 'Packaging 02', 'Packaging station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (6816, 'K3675', 'Packaging 03', 'Packaging station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (6821, 'K3675', 'Packaging 04', 'Packaging station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (6822, 'K3676', 'Packaging 05', 'Packaging station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (8167, 'D9123', 'Welding 01', 'Spot welding staion');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (8170, 'D9123', 'Welding 02', 'Spot welding staion');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (8171, 'D9123', 'Welding 03', 'Spot welding staion');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (7235, 'T3452', 'Assembly 01', 'Product assembly station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (7236, 'T3452', 'Assembly 02', 'Product assembly station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (7238, 'T3452', 'Assembly 03', 'Product assembly station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (5124, 'C5637', 'Trimming 01', 'Metal trimming station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (4123, 'Q3547', 'Polishing 01', 'Metal polishing station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (4124, 'Q3547', 'Polishing 02', 'Metal polishing station');
INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (4125, 'Q3547', 'Polishing 03', 'Metal polishing station');
-- Materials
INSERT INTO FactoryItem (item_id, description) VALUES ('PN12344A21', 'Screw M6 35 mm');
INSERT INTO Material (material_id) VALUES ('PN12344A21');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN52384R50', '300x300 mm 5 mm stainless steel sheet');
INSERT INTO Material (material_id) VALUES ('PN52384R50');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN52384R10', '300x300 mm 1 mm stainless steel sheet');
INSERT INTO Material (material_id) VALUES ('PN52384R10');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN18544A21', 'Rivet 6 mm');
INSERT INTO Material (material_id) VALUES ('PN18544A21');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN18544C21', 'Stainless steel handle model U6');
INSERT INTO Material (material_id) VALUES ('PN18544C21');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN18324C54', 'Stainless steel handle model R12');
INSERT INTO Material (material_id) VALUES ('PN18324C54');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN52384R45', '250x250 mm 5 mm stainless steel sheet');
INSERT INTO Material (material_id) VALUES ('PN52384R45');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN52384R12', '250x250 mm 1 mm stainless steel sheet');
INSERT INTO Material (material_id) VALUES ('PN52384R12');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN18324C91', 'Stainless steel handle model S26');
INSERT INTO Material (material_id) VALUES ('PN18324C91');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN18324C51', 'Stainless steel handle model R11');
INSERT INTO Material (material_id) VALUES ('PN18324C51');
INSERT INTO FactoryItem (item_id, description) VALUES ('PN94561L67', 'Coolube 2210XP');
INSERT INTO Material (material_id) VALUES ('PN94561L67');
-- Products
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945T22', 130, 'La Belle 22 5l pot');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945S22', 125, 'Pro 22 5l pot');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12946S22', 125, 'Pro 22 5l pot bottom');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12947S22', 145, 'Pro 22 lid');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945S20', 125, 'Pro 20 3l pot');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12946S20', '3l 20 cm stainless steel pot bottom');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12946S20', 125, 'Pro 20 3l pot bottom');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12947S20', '20 cm stainless steel lid');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12947S20', 145, 'Pro 20 lid');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945S17', '2l 17 cm stainless steel pot');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945S17', 125, 'Pro 17 2l pot');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945P17', '2l 17 cm stainless steel sauce pan');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945P17', 132, 'Pro 17 2l sauce pan');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945S48', '17 cm stainless steel lid');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945S48', 145, 'Pro 17 lid');
INSERT INTO FactoryItem (item_id, description) VALUES ('AS12945G48', '17 cm glass lid');
INSERT INTO FinalProduct (final_product_id, family_id, product_name) VALUES ('AS12945G48', 146, 'Pro Clear 17 lid');
-- Intermediary Products
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A01', '250 mm 5 mm stailess steel disc');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A01');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A02', '220 mm pot base phase 1');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A02');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A03', '220 mm pot base phase 2');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A03');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A04', '220 mm pot base final');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A04');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A01', '250 mm 1 mm stainless steel disc');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A01');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A02', '220 mm lid pressed');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A02');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A03', '220 mm lid polished');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A03');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A04', '220 mm lid with handle');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A04');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A32', '200 mm pot base phase 1');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A32');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A33', '200 mm pot base phase 2');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A33');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12945A34', '200 mm pot base final');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12945A34');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A32', '200 mm lid pressed');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A32');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A33', '200 mm lid polished');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A33');
INSERT INTO FactoryItem (item_id, description) VALUES ('IP12947A34', '200 mm lid with handle');
INSERT INTO IntermediaryProduct (iproduct_id) VALUES ('IP12947A34');
-- Operation Types
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5647, 'Disc cutting');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5649, 'Initial pot base pressing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5651, 'Final pot base pressing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5653, 'Pot base finishing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5655, 'Lid pressing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5657, 'Lid finishing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5659, 'Pot handles riveting');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5661, 'Lid handle screw');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5663, 'Pot test and packaging');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5665, 'Handle welding');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5667, 'Lid polishing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5669, 'Pot base polishing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5671, 'Teflon painting');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5681, 'Initial pan base pressing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5682, 'Final pan base pressing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5683, 'Pan base finishing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5685, 'Handle gluing');
INSERT INTO OperationType (op_type_id, operation_name) VALUES (5688, 'Pan test and packaging');
-- Operation Types Workstations
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5647, 'A4578', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5647, 'A4588', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5647, 'A4598', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5649, 'A4588', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5649, 'A4598', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5651, 'A4588', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5651, 'A4598', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5653, 'C5637', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5655, 'A4588', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5655, 'A4598', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5657, 'C5637', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5659, 'S3271', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5661, 'T3452', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5663, 'K3675', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5665, 'D9123', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5667, 'Q3547', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5669, 'Q3547', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5671, 'Q5478', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5681, 'A4588', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5681, 'A4598', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5682, 'A4588', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5682, 'A4598', 4000, 1200);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5683, 'C5637', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5685, 'D9123', 4000, 120);
INSERT INTO WorkstationType_Operation (op_type_id, workstation_type_id, maximum_execution_time, setup_time) VALUES (5688, 'K3675', 4000, 120);
-- Material Procurements
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12345, 'PN18544C21', TO_DATE('2023/10/01', 'yyyy/mm/dd'), NULL, 1.25, 20);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12345, 'PN18324C54', TO_DATE('2023/10/01', 'yyyy/mm/dd'), TO_DATE('2024/02/29', 'yyyy/mm/dd'), 1.70, 10);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12345, 'PN18324C54', TO_DATE('2024/04/01', 'yyyy/mm/dd'), NULL, 1.80, 16);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12345, 'PN18324C51', TO_DATE('2023/07/01', 'yyyy/mm/dd'), TO_DATE('2024/03/31', 'yyyy/mm/dd'), 1.90, 30);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12345, 'PN18324C51', TO_DATE('2024/04/01', 'yyyy/mm/dd'), NULL, 1.90, 20);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12298, 'PN18544C21', TO_DATE('2023/09/01', 'yyyy/mm/dd'), NULL, 1.35, 10);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12298, 'PN18324C54', TO_DATE('2023/08/01', 'yyyy/mm/dd'), TO_DATE('2024/01/29', 'yyyy/mm/dd'), 1.80, 10);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12298, 'PN18324C54', TO_DATE('2024/02/15', 'yyyy/mm/dd'), NULL, 1.75, 20);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12298, 'PN18324C51', TO_DATE('2023/08/01', 'yyyy/mm/dd'), TO_DATE('2024/05/31', 'yyyy/mm/dd'), 1.80, 40);
INSERT INTO MaterialProcurement (supplier_id, material_id, sale_start, sale_end, price, min_order_size)
VALUES (12298, 'PN12344A21', TO_DATE('2023/07/01', 'yyyy/mm/dd'), NULL, 0.65, 200);
-- AS12946S22 operations
INSERT INTO Operation VALUES (115, 5659, 'AS12946S22', 1, 'unit', NULL, 'AS12946S22', 600);
INSERT INTO Operation_InputList VALUES (115, 'IP12945A04', 1, 'unit');
INSERT INTO Operation_InputList VALUES (115, 'PN18544A21', 4, 'unit');
INSERT INTO Operation_InputList VALUES (115, 'PN18544C21', 2, 'unit');
INSERT INTO Operation VALUES (114, 5653, 'IP12945A04', 1, 'unit', 115, 'AS12946S22', 300);
INSERT INTO Operation_InputList VALUES (114, 'IP12945A03', 1, 'unit');
INSERT INTO Operation VALUES (112, 5651, 'IP12945A03', 1, 'unit', 114, 'AS12946S22', 120);
INSERT INTO Operation_InputList VALUES (112, 'IP12945A02', 1, 'unit');
INSERT INTO Operation_InputList VALUES (112, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (103, 5649, 'IP12945A02', 1, 'unit', 112, 'AS12946S22', 90);
INSERT INTO Operation_InputList VALUES (103, 'IP12945A01', 1, 'unit');
INSERT INTO Operation_InputList VALUES (103, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (100, 5647, 'IP12945A01', 1, 'unit', 103, 'AS12946S22', 120);
INSERT INTO Operation_InputList VALUES (100, 'PN52384R50', 1, 'unit');
-- AS12947S22 operations
INSERT INTO Operation VALUES (124, 5667, 'AS12947S22', 1, 'unit', NULL, 'AS12947S22', 1200);
INSERT INTO Operation_InputList VALUES (124, 'IP12947A04', 1, 'unit');
INSERT INTO Operation VALUES (123, 5661, 'IP12947A04', 1, 'unit', 124, 'AS12947S22', 150);
INSERT INTO Operation_InputList VALUES (123, 'IP12947A03', 1, 'unit');
INSERT INTO Operation_InputList VALUES (123, 'PN18324C54', 1, 'unit');
INSERT INTO Operation_InputList VALUES (123, 'PN12344A21', 3, 'unit');
INSERT INTO Operation VALUES (122, 5657, 'IP12947A03', 1, 'unit', 123, 'AS12947S22', 240);
INSERT INTO Operation_InputList VALUES (122, 'IP12947A02', 1, 'unit');
INSERT INTO Operation VALUES (121, 5655, 'IP12947A02', 1, 'unit', 122, 'AS12947S22', 60);
INSERT INTO Operation_InputList VALUES (121, 'IP12947A01', 1, 'unit');
INSERT INTO Operation_InputList VALUES (121, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (120, 5647, 'IP12947A01', 1, 'unit', 121, 'AS12947S22', 105);
INSERT INTO Operation_InputList VALUES (120, 'PN52384R10', 1, 'unit');
-- AS12945S22 operations
INSERT INTO Operation VALUES (130, 5663, 'AS12945S22', 1, 'unit', NULL, 'AS12945S22', 240);
INSERT INTO Operation_InputList VALUES (130, 'AS12947S22', 1, 'unit');
INSERT INTO Operation_InputList VALUES (130, 'AS12946S22', 1, 'unit');
-- AS12946S20 operations
INSERT INTO Operation VALUES (154, 5659, 'AS12946S20', 1, 'unit', NULL, 'AS12946S20', 600);
INSERT INTO Operation_InputList VALUES (154, 'IP12945A34', 1, 'unit');
INSERT INTO Operation_InputList VALUES (154, 'PN18544C21', 2, 'unit');
INSERT INTO Operation_InputList VALUES (154, 'PN18544A21', 4, 'unit');
INSERT INTO Operation VALUES (153, 5653, 'IP12945A34', 1, 'unit', 154, 'AS12946S20', 320);
INSERT INTO Operation_InputList VALUES (153, 'IP12945A33', 1, 'unit');
INSERT INTO Operation VALUES (152, 5651, 'IP12945A33', 1, 'unit', 153, 'AS12946S20', 120);
INSERT INTO Operation_InputList VALUES (152, 'IP12945A32', 1, 'unit');
INSERT INTO Operation_InputList VALUES (152, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (151, 5649, 'IP12945A32', 1, 'unit', 152, 'AS12946S20', 90);
INSERT INTO Operation_InputList VALUES (151, 'IP12945A01', 1, 'unit');
INSERT INTO Operation_InputList VALUES (151, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (150, 5647, 'IP12945A01', 1, 'unit', 151, 'AS12946S20', 120);
INSERT INTO Operation_InputList VALUES (150, 'PN52384R50', 1, 'unit');
-- AS12947S20 operations
INSERT INTO Operation VALUES (164, 5667, 'AS12947S20', 1, 'unit', NULL, 'AS12947S20', 1200);
INSERT INTO Operation_InputList VALUES (164, 'IP12947A34', 1, 'unit');
INSERT INTO Operation VALUES (163, 5661, 'IP12947A34', 1, 'unit', 164, 'AS12947S20', 150);
INSERT INTO Operation_InputList VALUES (163, 'IP12947A33', 1, 'unit');
INSERT INTO Operation_InputList VALUES (163, 'PN18324C51', 1, 'unit');
INSERT INTO Operation_InputList VALUES (163, 'PN12344A21', 3, 'unit');
INSERT INTO Operation VALUES (162, 5657, 'IP12947A33', 1, 'unit', 163, 'AS12947S20', 240);
INSERT INTO Operation_InputList VALUES (162, 'IP12947A32', 1, 'unit');
INSERT INTO Operation VALUES (161, 5655, 'IP12947A32', 1, 'unit', 162, 'AS12947S20', 60);
INSERT INTO Operation_InputList VALUES (161, 'IP12947A01', 1, 'unit');
INSERT INTO Operation_InputList VALUES (161, 'PN94561L67', 5, 'ml');
INSERT INTO Operation VALUES (160, 5647, 'IP12947A01', 1, 'unit', 161, 'AS12947S20', 90);
INSERT INTO Operation_InputList VALUES (160, 'PN52384R10', 1, 'unit');
-- AS12945S20 operations
INSERT INTO Operation VALUES (170, 5663, 'AS12945S20', 1, 'unit', NULL, 'AS12945S20', 240);
INSERT INTO Operation_InputList VALUES (170, 'AS12946S20', 1, 'unit');
INSERT INTO Operation_InputList VALUES (170, 'AS12947S20', 1, 'unit');
-- Order for 1 of AS12946S20
INSERT INTO CustomerOrder (order_id, customer_id, date_order, date_delivery, address_id)
VALUES (123, 456, TO_DATE('2023/10/01', 'yyyy/mm/dd'), TO_DATE('2023/10/15', 'yyyy/mm/dd'), 1);
INSERT INTO OrderProductList (order_id, product_id, quantity)
VALUES (123, 'AS12946S20', 2);
INSERT INTO OrderProductList (order_id, product_id, quantity)
VALUES (123, 'AS12947S20', 2);
INSERT INTO CustomerOrder (order_id, customer_id, date_order, date_delivery, address_id)
VALUES (124, 785, TO_DATE('2023/10/01', 'yyyy/mm/dd'), TO_DATE('2023/10/15', 'yyyy/mm/dd'), 2);
INSERT INTO OrderProductList (order_id, product_id, quantity)
VALUES (124, 'AS12946S22', 2);

INSERT INTO MaterialStock (material_id, qt_in_stock, min_stock_size)
VALUES ('PN18544C21', 100, 20);
INSERT INTO MaterialStock (material_id, qt_in_stock, min_stock_size)
VALUES ('PN18544A21', 100, 20);
INSERT INTO MaterialStock (material_id, qt_in_stock, min_stock_size)
VALUES ('PN94561L67', 100, 20);
INSERT INTO MaterialStock (material_id, qt_in_stock, min_stock_size)
VALUES ('PN52384R50', 100, 20);

-- Order for 30 of AS12946S20's
INSERT INTO CustomerOrder (order_id, customer_id, date_order, date_delivery, address_id)
VALUES (125, 456, TO_DATE('2023/10/01', 'yyyy/mm/dd'), TO_DATE('2023/10/15', 'yyyy/mm/dd'), 1);
INSERT INTO OrderProductList (order_id, product_id, quantity)
VALUES (125, 'AS12946S20', 30);

