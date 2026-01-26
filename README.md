# 🚀 Techpulse Booking Application

### A High-Concurrency Booking System with Race Condition Protection

---

## 📌 Overview

**Techpulse Booking Application** is a **backend-only system** built with **Spring Boot**, designed to handle **highly concurrent booking requests** while ensuring that **each slot can be booked only once**.

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

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Slot s WHERE s.id = :slotId")
Optional<Slot> findByIdForUpdate(Long slotId);
Why Pessimistic Locking?
Booking systems experience high contention

Guarantees strong consistency
Prevents double booking without retry logic
Works reliably even after application restarts

❌ In-memory locks (synchronized) are intentionally avoided as per assignment requirements.



🔁 Transaction Management
The booking operation is executed within a single transactional boundary:

@Transactional
public BookingResponse bookSlot(Long slotId)
Transaction Flow
Lock slot row

Check slot availability
Create booking record
Update slot status to BOOKED
Commit transaction

➡️ If any step fails, the entire transaction is rolled back automatically.
