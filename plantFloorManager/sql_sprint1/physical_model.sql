CREATE TABLE Customer (
  customer_id   number(10) NOT NULL, 
  name          varchar2(60) NOT NULL, 
  vatin         varchar2(25) NOT NULL UNIQUE, 
  address_id    number(10) NOT NULL, 
  type_id       varchar2(50) NOT NULL, 
  contact       varchar2(60), 
  email_address varchar2(60), 
  phone_number  varchar2(60), 
  PRIMARY KEY (customer_id));
CREATE TABLE CustomerOrder (
  order_id      number(10) NOT NULL, 
  customer_id   number(10) NOT NULL, 
  date_order    date NOT NULL, 
  date_delivery date NOT NULL, 
  PRIMARY KEY (order_id));
CREATE TABLE ProductFamily (
  product_family_name varchar2(40) NOT NULL, 
  PRIMARY KEY (product_family_name));
CREATE TABLE Product (
  product_id          varchar2(60) NOT NULL, 
  product_name        varchar2(255) NOT NULL, 
  product_family_name varchar2(40), 
  description         varchar2(255), 
  PRIMARY KEY (product_id));
CREATE TABLE BillOfMaterials (
  product_id   varchar2(60) NOT NULL, 
  component_id varchar2(60) NOT NULL, 
  quantity     number(10) NOT NULL, 
  PRIMARY KEY (product_id, 
  component_id));
CREATE TABLE BillOfOperations (
  product_family_name varchar2(40) NOT NULL, 
  operation_id        number(10) NOT NULL, 
  sequence_number     number(10) NOT NULL, 
  PRIMARY KEY (product_family_name, 
  operation_id, 
  sequence_number));
CREATE TABLE Workstation (
  workstation_id      number(10) NOT NULL, 
  workstation_type_id varchar2(70) NOT NULL, 
  name                varchar2(255) NOT NULL, 
  description         varchar2(255), 
  PRIMARY KEY (workstation_id));
CREATE TABLE WorkstationType (
  type_id   varchar2(70) NOT NULL, 
  type_name varchar2(60) NOT NULL, 
  PRIMARY KEY (type_id));
CREATE TABLE Address (
  address_id   number(10) NOT NULL, 
  address_name varchar2(255) NOT NULL, 
  zip_code     varchar2(60) NOT NULL, 
  town_name    varchar2(60) NOT NULL, 
  country_name varchar2(60) NOT NULL, 
  PRIMARY KEY (address_id));
CREATE TABLE Supplier (
  supplier_id   number(10) GENERATED AS IDENTITY, 
  supplier_name varchar2(60), 
  address_id    number(10) NOT NULL, 
  contact_id    number(10) NOT NULL, 
  PRIMARY KEY (supplier_id));
CREATE TABLE Operation (
  operation_id number(10) NOT NULL, 
  description  varchar2(255) NOT NULL, 
  PRIMARY KEY (operation_id));
CREATE TABLE CustomerType (
  type varchar2(50) NOT NULL, 
  PRIMARY KEY (type));
CREATE TABLE WorkstationTypeOperation (
  operation_id        number(10) NOT NULL, 
  workstation_type_id varchar2(70) NOT NULL, 
  PRIMARY KEY (operation_id, 
  workstation_type_id));
CREATE TABLE CustomerOrderProductList (
  order_id   number(10) NOT NULL, 
  product_id varchar2(60) NOT NULL, 
  quantity   number(10) NOT NULL, 
  PRIMARY KEY (order_id, 
  product_id));
ALTER TABLE BillOfMaterials ADD CONSTRAINT FKBillOfMate15104 FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE BillOfMaterials ADD CONSTRAINT FKBillOfMate812972 FOREIGN KEY (component_id) REFERENCES Product (product_id);
ALTER TABLE Customer ADD CONSTRAINT FKCustomer76555 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE CustomerOrder ADD CONSTRAINT FKCustomerOr152804 FOREIGN KEY (customer_id) REFERENCES Customer (customer_id);
ALTER TABLE Supplier ADD CONSTRAINT FKSupplier412300 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE Customer ADD CONSTRAINT FKCustomer985093 FOREIGN KEY (type_id) REFERENCES CustomerType (type);
ALTER TABLE Product ADD CONSTRAINT FKProduct917682 FOREIGN KEY (product_family_name) REFERENCES ProductFamily (product_family_name);
ALTER TABLE WorkstationTypeOperation ADD CONSTRAINT FKWorkstatio140333 FOREIGN KEY (operation_id) REFERENCES Operation (operation_id);
ALTER TABLE BillOfOperations ADD CONSTRAINT FKBillOfOper571673 FOREIGN KEY (operation_id) REFERENCES Operation (operation_id);
ALTER TABLE BillOfOperations ADD CONSTRAINT FKBillOfOper303253 FOREIGN KEY (product_family_name) REFERENCES ProductFamily (product_family_name);
ALTER TABLE WorkstationTypeOperation ADD CONSTRAINT FKWorkstatio104814 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE Workstation ADD CONSTRAINT FKWorkstatio963327 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE CustomerOrderProductList ADD CONSTRAINT FKCustomerOr444356 FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE CustomerOrderProductList ADD CONSTRAINT FKCustomerOr314310 FOREIGN KEY (order_id) REFERENCES CustomerOrder (order_id);
