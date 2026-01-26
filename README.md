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
✅ Pessimistic Locking (Database-Level)
🔹 What is Pessimistic Locking?

Pessimistic Locking is a database-level concurrency control technique where the system assumes conflicts can happen when multiple users try to access the same data simultaneously.

To avoid these conflicts, the database locks a record before it is updated, ensuring that only one transaction can modify the data at a time. Other transactions must wait until the lock is released.

In simple words:

The database locks the booking slot first, then allows the operation.

🔹 Why Pessimistic Locking is Important for This Project

This project involves booking operations, where:

Multiple users can try to book the same slot at the same time

Data accuracy is critical

Pessimistic locking is used because:

Booking systems have high contention

Strong consistency is required

Double booking must be completely prevented

🔹 How Pessimistic Locking is Achieved in This Project

In this project, pessimistic locking is achieved by:

Applying a write-level database lock on the booking slot when it is fetched for booking

Ensuring that once a slot is being processed by one transaction:

No other transaction can update the same slot

Other users must wait until the current booking operation finishes

Releasing the lock automatically when the transaction completes (commit or rollback)

This guarantees that only one booking operation can succeed for a slot at any given time.

🔹 Key Concepts Used (Important but No Code)
🔸 PESSIMISTIC_WRITE Lock

Ensures exclusive access to the slot record

Blocks other read-for-update or write operations

Prevents race conditions during booking

🔸 Transaction-Based Locking

The lock is active only during the transaction lifecycle

Automatically released after completion

Safe even if the application crashes or restarts

🔸 Database-Controlled Safety

Locking is handled by the database, not application memory

Works reliably in multi-user and multi-instance environments

🔹 Why In-Memory Locks Are Avoided

In-memory locking mechanisms like synchronized are intentionally avoided because:

They work only within a single JVM

They fail in distributed or scaled applications

Locks are lost after application restarts

They do not guarantee database consistency

Database-level pessimistic locking solves all these issues.
