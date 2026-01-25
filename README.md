Techpulse Booking Application
Booking System with Race Condition Protection
Overview

This project is a backend-only Booking System built using Spring Boot.
It is designed to handle multiple concurrent booking requests while guaranteeing that a slot can be booked only once.

The primary focus of this application is to prevent race conditions when multiple users attempt to book the same slot at the same time, ensuring data consistency and correctness.

No UI is implemented, as this is a backend-focused assignment.

Purpose of the Assignment

This assignment evaluates the following backend engineering concepts:

Database transactions

Concurrency and race condition handling

Database locking strategies

Spring transactional behavior

Role-based authorization

Real-world backend problem solving

Core Problem

When multiple users try to book the same slot concurrently, the system must:

Prevent double booking

Maintain consistent slot and booking data

Ensure atomic booking operations

Without proper concurrency control, parallel requests can lead to inconsistent and incorrect data states.

Roles and Responsibilities
USER

View available and booked slots

Book a slot only if it is available

Cancel only their own booking

ADMIN

Create new slots

View all slots

Cancel any booking

Domain Entities
Slot

id

startTime

endTime

status (AVAILABLE, BOOKED)

Booking

id

slot

user

status (ACTIVE, CANCELLED)

createdAt

Security Rules

All protected APIs require JWT authentication

Role-based authorization is enforced using Spring Security

USER can cancel only their own booking

ADMIN can cancel any booking

Unauthorized or forbidden requests return proper HTTP status codes

API Endpoints
Slot APIs

POST /slots

Role: ADMIN

Description: Create a new bookable slot

GET /slots

Role: USER, ADMIN

Description: Fetch all available and booked slots

Booking APIs

POST /bookings?slotId={id}

Role: USER

Description: Book a slot if available

POST /bookings/{id}/cancel

Role: USER

Description: Cancel own booking

POST /admin/bookings/{id}/cancel

Role: ADMIN

Description: Cancel any booking

Chosen Locking Strategy
Pessimistic Locking (Database-Level)

This project uses database-level pessimistic locking to prevent race conditions during booking.

Implementation approach:

The slot row is locked using PESSIMISTIC_WRITE before checking availability.

While one transaction holds the lock, other transactions must wait.

This ensures that only one booking can be created for a slot.

Reasons for choosing pessimistic locking:

Booking systems typically face high contention.

Guarantees strong consistency.

No retry logic is required.

Works correctly even after application restart.

In-memory locks or synchronized blocks are not used, as required by the assignment.

Transaction Boundaries

The booking operation runs inside a single transactional boundary.

Transaction flow:

Lock the slot row

Check slot availability

Create booking record

Update slot status to BOOKED

Commit transaction

If any step fails, the entire transaction is rolled back automatically.

How Race Conditions Are Prevented
Without Locking

User A reads slot as AVAILABLE

User B reads slot as AVAILABLE

Both users create bookings

Result: Double booking (incorrect behavior)

With Pessimistic Locking

User A locks the slot row

User B waits for the lock

User A completes booking and commits

User B receives “Slot already booked” error

This guarantees that only one booking per slot is possible.

Example Concurrent Booking Scenario

Test setup:

Two users logged in with valid JWT tokens

Same slotId

Requests sent almost simultaneously using Postman

Expected result:

First request succeeds and books the slot

Second request fails with a “Slot already booked” message

This confirms that race condition protection works correctly.

Error Handling

Centralized global exception handling

Clear and consistent error responses

Proper HTTP status codes:

400 – Validation or business errors

401 – Unauthorized access

403 – Forbidden access

409 – Booking conflict

Technology Stack

Java 17

Spring Boot

Spring Security

Spring Data JPA

H2 Database

JWT (JSON Web Token)

Maven

Lombok

Steps to Run the Application

Clone the repository:

git clone <github-repository-url>


Navigate to the project directory:

cd techpulse-booking-application


Run the application:

mvn spring-boot:run


Application runs on:

http://localhost:8082


H2 console is enabled for debugging and testing.

Unit Testing

Unit tests are written for services and controllers

Concurrency scenarios are tested

Target test coverage is at least 80 percent

Tools used:

Spring Boot Test

Mockito

Conclusion

This project demonstrates a real-world backend design that focuses on:

Strong concurrency control

Data consistency

Secure role-based authorization

Clean and maintainable architecture

The use of database-level pessimistic locking ensures that the system behaves correctly even under high concurrent load.
