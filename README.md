# 🚀 High Concurrency Booking Application

### A High-Concurrency Booking System with Race Condition Protection

---

## 📌 Overview

**High Concurrency Booking Application** is a **backend-only system** built with **Spring Boot**, designed to handle **highly concurrent booking requests** while ensuring that **each slot can be booked only once**.

The core objective of this application is to **prevent race conditions** when multiple users attempt to book the same slot simultaneously, while guaranteeing:

- ✅ Data Consistency  
- ✅ Correctness  
- ✅ Reliability under High Concurrency  

This project closely simulates **real-world production challenges** faced in booking systems such as **event reservations**, **appointment scheduling**, and **ticketing platforms**.

---

## 🎯 Purpose of the Assignment

This assignment evaluates **practical backend engineering skills**, including:

- Database transactions  
- Concurrency & race condition handling  
- Database locking strategies  
- Spring transactional behavior  
- Secure role-based authorization  
- Production-grade backend problem solving  

---

## 🧠 Core Problem Statement

When multiple users try to book the **same slot at the same time**, the system must:

- Prevent double booking  
- Maintain consistent slot and booking data  
- Ensure **atomic booking operations**  

Without proper concurrency control, parallel requests can cause **data corruption**, which is unacceptable in real-world systems.

---

## 👥 Roles & Responsibilities

### 👤 USER
- View available and booked slots  
- Book a slot *(only if available)*  
- Cancel **only their own** booking  

### 👑 ADMIN
- Create new slots  
- View all slots  
- Cancel **any** booking  

---

## 🗃️ Domain Model

### 🕒 Slot Entity

| Field       | Description              |
|------------|--------------------------|
| `id`        | Unique identifier        |
| `startTime` | Slot start time          |
| `endTime`   | Slot end time            |
| `status`    | AVAILABLE / BOOKED       |

---

### 📘 Booking Entity

| Field        | Description                   |
|-------------|-------------------------------|
| `id`         | Unique identifier             |
| `slot`       | Associated slot               |
| `user`       | Booking owner                 |
| `status`     | ACTIVE / CANCELLED            |
| `createdAt`  | Timestamp of booking          |

---

## 🔐 Security Rules

- All protected APIs require **JWT Authentication**
- Role-based authorization enforced using **Spring Security**
- USER can cancel **only their own** booking
- ADMIN can cancel **any** booking
- Proper HTTP status codes returned:
  - `401` – Unauthorized
  - `403` – Forbidden

---

## 🔗 API Endpoints

### 🕒 Slot APIs

| Method | Endpoint | Role |
|------|----------|------|
| POST | `/slots` | ADMIN |
| GET  | `/slots` | USER, ADMIN |

---

### 📘 Booking APIs

| Method | Endpoint | Role |
|------|----------|------|
| POST | `/bookings?slotId={id}` | USER |
| POST | `/bookings/{id}/cancel` | USER |
| POST | `/admin/bookings/{id}/cancel` | ADMIN |

---
## 🔒 Concurrency Control Strategy
### ✅ Pessimistic Locking (Database-Level)

The application uses **PESSIMISTIC_WRITE locking** to prevent race conditions during booking operations.
In this project, database-level pessimistic locking is implemented using
@Lock(LockModeType.PESSIMISTIC_WRITE) to ensure exclusive access to a booking slot during the booking process.

 ## Why Pessimistic Locking?

Booking systems experience high contention, where multiple users may attempt to book the same slot at the same time
Guarantees strong consistency by allowing only one transaction to modify a slot at a time
Prevents double booking without requiring retry logic
Works reliably even after application restarts, as locks are managed by the database


##❌ In-memory locks (e.g., synchronized) are intentionally avoided as per assignment requirements because they are JVM-scoped, unreliable in distributed systems, and unsafe across restarts.





## 🔁 Transaction Management

The slot booking operation is executed within a **single transactional boundary** to ensure **data consistency and reliability**.

All database operations involved in booking a slot are treated as **one atomic unit**.  
This means **either all steps succeed, or none of them are applied**.

---

### 🔄 Transaction Flow

1. **Lock the slot record**  
   The selected slot is locked at the **database level** to prevent other users from modifying it during the booking process.

2. **Check slot availability**  
   The system verifies whether the slot is still available before proceeding.

3. **Create booking record**  
   A new booking entry is created **only if the slot is available**.

4. **Update slot status**  
   The slot status is updated from `AVAILABLE` to `BOOKED`.

5. **Commit transaction**  
   Once all steps complete successfully, the transaction is committed and changes are permanently saved.

---

### 🔁 Automatic Rollback Handling

➡️ If **any step fails** (for example: slot already booked, database error, or validation failure), **the entire transaction is rolled back automatically**.

This ensures:

- ❌ No partial data is saved  
- ❌ No inconsistent booking states  
- ❌ No double booking issues  

---

### ❓ Why Transaction Management Is Important

- Maintains **atomicity** (all-or-nothing behavior)
- Ensures **data integrity**
- Works reliably in **high-concurrency environments**
- Handles failures safely **without manual cleanup**

---

### ✅ RESULT : 

Using a **single transactional boundary** guarantees that slot booking is **safe, consistent, and fault-tolerant**, making the system reliable even under **heavy user load**.




## 🛡️ Race Condition Prevention Explained

### ❌ Without Locking

- User A reads slot as **AVAILABLE**
- User B reads slot as **AVAILABLE**
- Both create bookings  
- ❌ **Double booking occurs**

---

### ✅ With Pessimistic Locking

- User A locks the slot row  
- User B waits  
- User A completes booking  
- User B receives **"Slot already booked"**

➡️ **Guaranteed: Only one booking per slot**

---

## 🧪 Concurrent Booking Test Scenario

### Test Setup

- Two users authenticated with **JWT**
- Same `slotId`
- Requests sent simultaneously using **Postman**

### Expected Result

| User   | Result |
|--------|--------|
| User 1 | ✅ Booking Successful |
| User 2 | ❌ Slot Already Booked |

✔️ Confirms correct race condition handling.

---

## 🧾 Error Handling

- Centralized **Global Exception Handler**
- Clean and user-friendly error responses
- Proper HTTP status codes:

| Code | Meaning |
|------|---------|
| 400  | Validation / Business Error |
| 401  | Unauthorized |
| 403  | Forbidden |
| 409  | Booking Conflict |

---

## 🛠️ Technology Stack

- Java 17  
- Spring Boot  
- Spring Security  
- Spring Data JPA  
- H2 Database
- AWS(EC2)
- JWT (JSON Web Token)  
- Maven  
- Lombok  

---




## ▶️ Running the Application

Follow the steps below to run the **Techpulse Booking Application** locally.

---

## ✅ Prerequisites

Ensure the following are installed on your system:

* **Java 17** 
* **Maven**
* **Git**

---

## 🚀 Steps to Run

### 1️⃣ Clone the Repository

```bash
git clone <github-repository-url>
```

### 2️⃣ Navigate to the Project Directory

```bash
cd techpulse-booking-application
```

### 3️⃣ Build and Run the Application

```bash
mvn spring-boot:run
```

---

## 🌐 Access the Application

* **AWS EC2 Server URL:** [[http://localhost:8082](http://3.109.181.232:8082)]

* * **▶ *Loom Video Link:*:** [[[http://localhost:8082](http://3.109.181.232:8082)](https://www.loom.com/share/4e1d6fcf8fb240778656ceec674bf8ad)]

---

## 🗄️ H2 Database Console

The H2 console is enabled for **debugging and testing purposes**.

* **H2 Console URL:** [http://3.109.181.232:8082/h2-console/](http://localhost:8082/h2-console)
* **JDBC URL, Username, Password:** Available in `application.properties`


---

## 🧪 Testing

The project includes **comprehensive testing** to ensure correctness, reliability, and concurrency safety.

### ✅ Test Coverage

* Unit tests for **Controller** and **Service** layers
* Concurrency scenarios tested to validate **locking behavior**


### 🛠️ Testing Tools Used

* **Spring Boot Test** – Integration and context-based testing
* **Mockito** – Mocking dependencies and isolated unit testing

---

## 🔐 Key Features

* 🔒 **Database-level pessimistic locking** for concurrency control
* 🔁 **Strong transactional integrity** using Spring transactions
* 🔐 **Secure role-based access control**
* ⚡ Designed for **high-traffic booking systems**

---

## ✅ Conclusion

The **Techpulse Booking Application** demonstrates a robust and scalable approach to solving concurrency challenges in booking systems. By leveraging:

* 🔒 Pessimistic database locking
* 🔁 Transactional consistency
* 🔐 Secure access control

This project reflects **real-world backend engineering practices** and is well-suited for **consistency-critical, high-concurrency applications**.

---

### 📌 Author

**Krishna Jadhav**

Feel free to contribute, raise issues, or suggest improvements 🚀
