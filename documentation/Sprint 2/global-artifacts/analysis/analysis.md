# OO Analysis

The construction process of the domain model is based on the client specifications, especially the nouns (for
_concepts_) and verbs (for _relations_) used.

## Rationale to identify domain conceptual classes

To identify domain conceptual classes, start by making a list of candidate conceptual classes inspired by the list of
categories suggested in the book "Applying UML and Patterns: An Introduction to Object-Oriented Analysis and Design and
Iterative Development".

### _Conceptual Class Category List_

**Business Transactions**

* Planning, construction and maintenance of green spaces
* Task

---

**Transaction Line Itemss**

*

---

**Product/Service related to a Transaction or Transaction Line Item**

*

---

**Transaction Records**

*

---  

**Roles of People or Organizations**

* Production Manager
* Plant Floor Manager
* 

---

**Places**

* Green Spaces
* Garden
* Park

---

**Noteworthy Events**

* 

---

**Physical Objects**

* Product

---

**Descriptions of Things**

*

---

**Catalogs**

*

---

**Containers**

* 

---

**Elements of Containers**

* 

---

**Organizations**



---

**Other External/Collaborating Systems**

*

---

**Records of finance, work, contracts, legal matters**

* 

---

**Financial Instruments**

*

---

**Documents mentioned/used to perform some work/**

*

---

## Rationale to identify associations between conceptual classes

An association is a relationship between instances of objects that indicates a relevant connection and that is worth of
remembering, or it is derivable from the List of Common Associations:

- **_A_** is physically or logically part of **_B_**
- **_A_** is physically or logically contained in/on **_B_**
- **_A_** is a description for **_B_**
- **_A_** known/logged/recorded/reported/captured in **_B_**
- **_A_** uses or manages or owns **_B_**
- **_A_** is related with a transaction (item) of **_B_**
- etc.


| Concept (A) 		 | Association   	 | Concept (B) |
|----------------|:---------------:|------------:|
|                |                 |             |


## Domain Model

**Do NOT forget to identify concept atributes too.**

**Insert below the Domain Model Diagram in a SVG format**

![Domain Model](svg/domain-data-model-puml.svg)

## Logical Model

![Logical Model](svg/logical-data-model.svg)
