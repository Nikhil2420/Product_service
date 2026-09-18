# E-Commerce Product Service

A backend REST API for an E-commerce application built using **Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL**.

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* REST APIs
* Bean Validation

## Features

* Product CRUD operations
* Category management
* User management
* Cart management
* Add/remove products from cart
* Purchase products
* Purchase history
* Stock management
* Payment status management
* Global exception handling
* Request validation
* Transaction management

## Project Structure

```text
src/main/java/com/ProductService/backend

├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config
```

## Database

The application uses **PostgreSQL**.

Main entities:

* User
* Product
* Category
* Cart
* Purchase
* Address

## Running the Application

### 1. Create Database

```sql
CREATE DATABASE ProductBackend;
```

### 2. Configure PostgreSQL

Update your database credentials in:

```text
src/main/resources/application.properties
```

### 3. Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or run `ProductServiceApplication` directly from IntelliJ IDEA.

The application runs on:

```text
http://localhost:8080
```

## API Examples

```text
GET    /products
GET    /products/{id}
POST   /products
PUT    /products/{id}
DELETE /products/{id}

POST   /cart/add
GET    /cart/{userId}

POST   /purchases/product
GET    /purchases/{purchaseId}
GET    /purchases/history/{userId}
```

## Author

**Nikhil Kumar Tiwari**

Java Backend Developer
