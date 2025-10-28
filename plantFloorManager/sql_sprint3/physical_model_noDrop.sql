CREATE TABLE Customer (
  customer_id   number(10) NOT NULL, 
  name          varchar2(60) NOT NULL, 
  vatin         varchar2(25) NOT NULL UNIQUE, 
  address_id    number(10) NOT NULL, 
  type_id       number(3) NOT NULL, 
  email_address varchar2(60), 
  phone_number  varchar2(60), 
  status        number(1) NOT NULL, 
  PRIMARY KEY (customer_id));
CREATE TABLE CustomerOrder (
  order_id      number(10) NOT NULL, 
  customer_id   number(10) NOT NULL, 
  date_order    date NOT NULL, 
  date_delivery date NOT NULL, 
  address_id    number(10) NOT NULL, 
  PRIMARY KEY (order_id));
CREATE TABLE ProductFamily (
  family_id   number(3) NOT NULL, 
  family_name varchar2(40) NOT NULL, 
  PRIMARY KEY (family_id));
CREATE TABLE Workstation (
  workstation_id      number(5) NOT NULL, 
  workstation_type_id varchar2(10) NOT NULL, 
  name                varchar2(100) NOT NULL, 
  description         varchar2(255) NOT NULL, 
  PRIMARY KEY (workstation_id));
CREATE TABLE WorkstationType (
  type_id   varchar2(10) NOT NULL, 
  type_name varchar2(60) NOT NULL, 
  PRIMARY KEY (type_id));
CREATE TABLE Address (
  address_id   number(10) NOT NULL, 
  address_name varchar2(255) NOT NULL, 
  zip_code     varchar2(30) NOT NULL, 
  town_name    varchar2(255) NOT NULL, 
  country_name varchar2(255) NOT NULL, 
  PRIMARY KEY (address_id));
CREATE TABLE Operation (
  operation_id            number(10) NOT NULL, 
  op_type_id              number(10) NOT NULL, 
  out_product_id          varchar2(15) NOT NULL, 
  out_quantity            number(10) NOT NULL, 
  out_quantity_unit       varchar2(10) NOT NULL, 
  next_op_id              number(10), 
  final_product_id        varchar2(15) NOT NULL, 
  expected_execution_time number(10) NOT NULL, 
  PRIMARY KEY (operation_id));
CREATE TABLE CustomerType (
  type_id number(3) NOT NULL, 
  type    varchar2(50) NOT NULL, 
  PRIMARY KEY (type_id));
CREATE TABLE WorkstationType_Operation (
  op_type_id             number(10) NOT NULL, 
  workstation_type_id    varchar2(10) NOT NULL, 
  maximum_execution_time number(10) NOT NULL, 
  setup_time             number(10) NOT NULL, 
  PRIMARY KEY (op_type_id, 
  workstation_type_id));
CREATE TABLE OrderProductList (
  order_id   number(10) NOT NULL, 
  product_id varchar2(15) NOT NULL, 
  quantity   number(10) NOT NULL, 
  PRIMARY KEY (order_id, 
  product_id));
CREATE TABLE Supplier (
  supplier_id         number(10) NOT NULL, 
  supplier_name       varchar2(60) NOT NULL, 
  supplier_address_id number(10) NOT NULL, 
  PRIMARY KEY (supplier_id));
CREATE TABLE MaterialProcurement (
  supplier_id    number(10) NOT NULL, 
  material_id    varchar2(15) NOT NULL, 
  sale_start     date NOT NULL, 
  sale_end       date, 
  price          number(10, 2) NOT NULL, 
  min_order_size number(10) NOT NULL, 
  PRIMARY KEY (supplier_id, 
  material_id, 
  sale_start));
CREATE TABLE Material (
  material_id varchar2(15) NOT NULL, 
  PRIMARY KEY (material_id));
CREATE TABLE FinalProduct (
  final_product_id varchar2(15) NOT NULL, 
  family_id        number(3) NOT NULL, 
  product_name     varchar2(100) NOT NULL, 
  PRIMARY KEY (final_product_id));
CREATE TABLE IntermediaryProduct (
  iproduct_id varchar2(15) NOT NULL, 
  PRIMARY KEY (iproduct_id));
CREATE TABLE FactoryItem (
  item_id     varchar2(15) NOT NULL, 
  description varchar2(255) NOT NULL, 
  PRIMARY KEY (item_id));
CREATE TABLE Operation_InputList (
  operation_id number(10) NOT NULL, 
  input_id     varchar2(15) NOT NULL, 
  quantity     number(10) NOT NULL, 
  qt_unit      varchar2(10) NOT NULL, 
  PRIMARY KEY (operation_id, 
  input_id));
CREATE TABLE MaterialStock (
  material_id    varchar2(15) NOT NULL, 
  qt_in_stock    number(10) NOT NULL, 
  min_stock_size number(10) NOT NULL, 
  PRIMARY KEY (material_id));
CREATE TABLE MaterialReservation (
  order_id    number(10) NOT NULL, 
  material_id varchar2(15) NOT NULL, 
  quantity    number(10) NOT NULL, 
  PRIMARY KEY (order_id, 
  material_id));
CREATE TABLE OperationType (
  op_type_id     number(10) NOT NULL, 
  operation_name varchar2(255) NOT NULL, 
  PRIMARY KEY (op_type_id));
ALTER TABLE Customer ADD CONSTRAINT FKCustomer76555 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE CustomerOrder ADD CONSTRAINT FKCustomerOr152804 FOREIGN KEY (customer_id) REFERENCES Customer (customer_id);
ALTER TABLE WorkstationType_Operation ADD CONSTRAINT FKWorkstatio210249 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE Workstation ADD CONSTRAINT FKWorkstatio963327 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE OrderProductList ADD CONSTRAINT FKOrderProdu809788 FOREIGN KEY (order_id) REFERENCES CustomerOrder (order_id);
ALTER TABLE CustomerOrder ADD CONSTRAINT FKCustomerOr562623 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE Customer ADD CONSTRAINT FKCustomer318387 FOREIGN KEY (type_id) REFERENCES CustomerType (type_id);
ALTER TABLE Supplier ADD CONSTRAINT FKSupplier646708 FOREIGN KEY (supplier_address_id) REFERENCES Address (address_id);
ALTER TABLE MaterialProcurement ADD CONSTRAINT FKMaterialPr232349 FOREIGN KEY (supplier_id) REFERENCES Supplier (supplier_id);
ALTER TABLE Operation ADD CONSTRAINT FKOperation102109 FOREIGN KEY (next_op_id) REFERENCES Operation (operation_id);
ALTER TABLE FinalProduct ADD CONSTRAINT FKFinalProdu639418 FOREIGN KEY (family_id) REFERENCES ProductFamily (family_id);
ALTER TABLE OrderProductList ADD CONSTRAINT FKOrderProdu151551 FOREIGN KEY (product_id) REFERENCES FinalProduct (final_product_id);
ALTER TABLE Operation_InputList ADD CONSTRAINT FKOperation_511997 FOREIGN KEY (operation_id) REFERENCES Operation (operation_id);
ALTER TABLE IntermediaryProduct ADD CONSTRAINT FKIntermedia616850 FOREIGN KEY (iproduct_id) REFERENCES FactoryItem (item_id);
ALTER TABLE FinalProduct ADD CONSTRAINT FKFinalProdu393735 FOREIGN KEY (final_product_id) REFERENCES FactoryItem (item_id);
ALTER TABLE Operation ADD CONSTRAINT FKOperation751934 FOREIGN KEY (out_product_id) REFERENCES FactoryItem (item_id);
ALTER TABLE MaterialReservation ADD CONSTRAINT FKMaterialRe960575 FOREIGN KEY (order_id) REFERENCES CustomerOrder (order_id);
ALTER TABLE Operation ADD CONSTRAINT FKOperation735897 FOREIGN KEY (op_type_id) REFERENCES OperationType (op_type_id);
ALTER TABLE WorkstationType_Operation ADD CONSTRAINT FKWorkstatio653148 FOREIGN KEY (op_type_id) REFERENCES OperationType (op_type_id);
ALTER TABLE Material ADD CONSTRAINT FKMaterial936028 FOREIGN KEY (material_id) REFERENCES FactoryItem (item_id);
ALTER TABLE MaterialStock ADD CONSTRAINT FKMaterialSt734974 FOREIGN KEY (material_id) REFERENCES Material (material_id);
ALTER TABLE Operation_InputList ADD CONSTRAINT FKOperation_921432 FOREIGN KEY (input_id) REFERENCES FactoryItem (item_id);
ALTER TABLE MaterialReservation ADD CONSTRAINT FKMaterialRe364030 FOREIGN KEY (material_id) REFERENCES Material (material_id);
ALTER TABLE MaterialProcurement ADD CONSTRAINT FKMaterialPr671364 FOREIGN KEY (material_id) REFERENCES Material (material_id);
ALTER TABLE Operation ADD CONSTRAINT FKOperation216974 FOREIGN KEY (final_product_id) REFERENCES FinalProduct (final_product_id);
