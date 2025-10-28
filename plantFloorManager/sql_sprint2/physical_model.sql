DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE CustomerOrder CASCADE CONSTRAINTS;
DROP TABLE ProductFamily CASCADE CONSTRAINTS;
DROP TABLE Product CASCADE CONSTRAINTS;
DROP TABLE Product_SubProducts CASCADE CONSTRAINTS;
DROP TABLE Workstation CASCADE CONSTRAINTS;
DROP TABLE WorkstationType CASCADE CONSTRAINTS;
DROP TABLE Address CASCADE CONSTRAINTS;
DROP TABLE Operation CASCADE CONSTRAINTS;
DROP TABLE CustomerType CASCADE CONSTRAINTS;
DROP TABLE WorkstationType_Operation CASCADE CONSTRAINTS;
DROP TABLE CustomerOrder_Products CASCADE CONSTRAINTS;
DROP TABLE Material CASCADE CONSTRAINTS;
DROP TABLE Product_Materials CASCADE CONSTRAINTS;
DROP TABLE Product_Operation CASCADE CONSTRAINTS;
ALTER SYSTEM SET open_cursors = 1200 SCOPE=BOTH;
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
  product_family_id   number(3) NOT NULL, 
  product_family_name varchar2(40) NOT NULL, 
  PRIMARY KEY (product_family_id));
CREATE TABLE Product (
  product_id        varchar2(60) NOT NULL, 
  product_name      varchar2(255) NOT NULL, 
  product_family_id number(3) NOT NULL, 
  description       varchar2(255) NOT NULL, 
  PRIMARY KEY (product_id));
CREATE TABLE Product_SubProducts (
  product_id    varchar2(60) NOT NULL, 
  subproduct_id varchar2(60) NOT NULL, 
  quantity      float(10) NOT NULL, 
  unit          varchar2(25) NOT NULL, 
  PRIMARY KEY (product_id, 
  subproduct_id));
CREATE TABLE Workstation (
  workstation_id      number(5) NOT NULL, 
  workstation_type_id varchar2(10) NOT NULL, 
  name                varchar2(100) NOT NULL, 
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
  operation_id number(10) NOT NULL, 
  description  varchar2(255) NOT NULL, 
  PRIMARY KEY (operation_id));
CREATE TABLE CustomerType (
  type_id number(3) NOT NULL, 
  type    varchar2(50) NOT NULL, 
  PRIMARY KEY (type_id));
CREATE TABLE WorkstationType_Operation (
  operation_id        number(10) NOT NULL, 
  workstation_type_id varchar2(10) NOT NULL, 
  PRIMARY KEY (operation_id, 
  workstation_type_id));
CREATE TABLE CustomerOrder_Products (
  order_id   number(10) NOT NULL, 
  product_id varchar2(60) NOT NULL, 
  quantity   number(10) NOT NULL, 
  PRIMARY KEY (order_id, 
  product_id));
CREATE TABLE Material (
  material_id   varchar2(15) NOT NULL, 
  material_name varchar2(100) NOT NULL, 
  description   varchar2(100) NOT NULL, 
  PRIMARY KEY (material_id));
CREATE TABLE Product_Materials (
  product_id  varchar2(60) NOT NULL, 
  material_id varchar2(15) NOT NULL, 
  quantity    float(10) NOT NULL, 
  unit        varchar2(25) NOT NULL, 
  PRIMARY KEY (product_id, 
  material_id));
CREATE TABLE Product_Operation (
  operation_id      number(10) NOT NULL, 
  output_product_id varchar2(60) NOT NULL, 
  quantity          float(10) NOT NULL, 
  unit              varchar2(25) NOT NULL, 
  PRIMARY KEY (operation_id, 
  output_product_id));
ALTER TABLE Product_SubProducts ADD CONSTRAINT FKProduct_Su626234 FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE Product_SubProducts ADD CONSTRAINT FKProduct_Su971345 FOREIGN KEY (subproduct_id) REFERENCES Product (product_id);
ALTER TABLE Customer ADD CONSTRAINT FKCustomer76555 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE CustomerOrder ADD CONSTRAINT FKCustomerOr152804 FOREIGN KEY (customer_id) REFERENCES Customer (customer_id);
ALTER TABLE WorkstationType_Operation ADD CONSTRAINT FKWorkstatio245768 FOREIGN KEY (operation_id) REFERENCES Operation (operation_id);
ALTER TABLE WorkstationType_Operation ADD CONSTRAINT FKWorkstatio210249 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE Workstation ADD CONSTRAINT FKWorkstatio963327 FOREIGN KEY (workstation_type_id) REFERENCES WorkstationType (type_id);
ALTER TABLE CustomerOrder_Products ADD CONSTRAINT FKCustomerOr864585 FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE CustomerOrder_Products ADD CONSTRAINT FKCustomerOr734539 FOREIGN KEY (order_id) REFERENCES CustomerOrder (order_id);
ALTER TABLE Product_Materials ADD CONSTRAINT FKProduct_Ma95848 FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE Product_Materials ADD CONSTRAINT FKProduct_Ma149151 FOREIGN KEY (material_id) REFERENCES Material (material_id);
ALTER TABLE CustomerOrder ADD CONSTRAINT FKCustomerOr562623 FOREIGN KEY (address_id) REFERENCES Address (address_id);
ALTER TABLE Customer ADD CONSTRAINT FKCustomer318387 FOREIGN KEY (type_id) REFERENCES CustomerType (type_id);
ALTER TABLE Product ADD CONSTRAINT FKProduct793891 FOREIGN KEY (product_family_id) REFERENCES ProductFamily (product_family_id);
ALTER TABLE Product_Operation ADD CONSTRAINT FKProduct_Op994299 FOREIGN KEY (output_product_id) REFERENCES Product (product_id);
ALTER TABLE Product_Operation ADD CONSTRAINT FKProduct_Op285278 FOREIGN KEY (operation_id) REFERENCES Operation (operation_id);
