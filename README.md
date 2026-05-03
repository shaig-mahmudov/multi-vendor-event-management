<div align="center">

# CrowdGo

### Multi-Vendor Event Management Platform — Backend API
> A production-grade RESTful backend API for a multi-vendor event marketplace — handling user authentication, vendor onboarding, event lifecycle management, ticket booking, reviews, and social interactions.


## Tech Stack
<p>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
</p>

<p>
  <img src="https://img.shields.io/badge/status-active-success?style=flat-square" />
  <img src="https://img.shields.io/badge/license-MIT-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=flat-square" />
</p>

<p>
  <a href="https://trello.com/b/HbirDhQD/crowdgo">
    <img src="https://img.shields.io/badge/Trello-Project_Board-0052CC?style=for-the-badge&logo=trello&logoColor=white" />
  </a>
</p>

</div>

---

## 📌 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Project Management](#project-management)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [API Overview](#api-overview)
- [Security Design](#security-design)
- [Future Improvements](#future-improvements)
- [Authors](#author)

---

## Overview

**CrowdGo** is a backend REST API that powers a multi-vendor event management ecosystem. It enables:

- **Attendees** to discover events, purchase tickets, leave reviews, and follow vendors.
- **Vendors** to register, list services, and manage their public profile.
- **Event Organizers** to create and manage events with ticket categories, manage co-organizers, and track bookings.
- **Admins** to moderate vendors, manage users, and maintain platform integrity through an audit trail.

The system is built with a strong emphasis on **security**, **clean architecture**, and **scalability** — following production-grade best practices including JWT token rotation, refresh token management, token blacklisting, brute-force awareness, and email verification workflows.

---

## Key Features

### Authentication & Security
- Stateless JWT-based authentication (Access + Refresh token pair)
- Refresh token rotation with secure invalidation
- JWT blacklist to prevent reuse of revoked tokens
- Email verification on registration
- Password reset via time-limited, single-use tokens
- Spring Security with role-based access control (RBAC)

### User Management
- Full user registration and profile update flows
- Role system: `USER`, `VENDOR`, `ADMIN`
- Admin panel to update roles, suspend, or delete accounts
- Audit log service to track privileged admin actions

### Vendor Management
- Vendor registration with moderation workflow (`PENDING` → `APPROVED` / `REJECTED`)
- Admin approval/rejection of vendor applications
- Public vendor profiles with follow/unfollow functionality

### Event Management
- Full event CRUD with status management (`DRAFT`, `PUBLISHED`, `CANCELLED`)
- Event categorization system
- Ticket category management with pricing and capacity
- Co-organizer support with ownership transfer capability

### Booking System
- Multi-item booking creation (multiple ticket categories per booking)
- Booking status tracking (`PENDING`, `CONFIRMED`, `CANCELLED`)
- Item-level booking detail responses

### Reviews & Social
- Post, update, and delete reviews per event
- Favorite events and vendor follow/unfollow interactions

### Email Service
- Gmail SMTP integration via `JavaMailSender`
- HTML email templates via Thymeleaf
- Email verification and password reset emails with dynamic token URLs
- Profile-based switching between mock and production mail service

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JJWT 0.12.6 |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Email | Spring Boot Mail (Gmail SMTP) |
| Templating | Thymeleaf |
| Validation | Spring Boot Validation (JSR-380) |
| Build Tool | Apache Maven |
| Utilities | Lombok |
| Config | Spring Dotenv (`spring-dotenv`) |
| Testing | Spring Boot Test + Spring Security Test |

---

## 🏛 Architecture

CrowdGo follows a **layered, domain-driven architecture** with a clean separation of concerns:

```
┌─────────────────────────────────────┐
│            REST Controllers          │  ← HTTP boundary, request/response mapping
├─────────────────────────────────────┤
│          Service Layer (Interfaces)  │  ← Business logic, orchestration
├─────────────────────────────────────┤
│        Repository Layer (JPA)        │  ← Data access, Spring Data
├─────────────────────────────────────┤
│          Domain Model (Entities)     │  ← JPA entities, enums
└─────────────────────────────────────┘
```

Each domain exposes a **service interface** with a separate **implementation class**, enabling easy testability and future extension (e.g. swapping implementations without touching the controller layer).

---

## 📁 Project Structure

```
multi-vendor-event-management/
└── src/main/java/org/ironhack/project/eventmanagement/
    │
    ├── controller/               # REST API endpoints
    │   ├── admin/                # AdminUserController, AdminVendorController
    │   ├── BookingController
    │   ├── CategoryController
    │   ├── EventController
    │   ├── OrganizerController
    │   ├── ReviewController
    │   ├── TicketCategoryController
    │   ├── UserController
    │   ├── UserInteractionController
    │   └── VendorController
    │
    ├── service/                  # Business logic (interface + impl pattern)
    │   ├── admin/
    │   ├── audit/
    │   ├── booking/
    │   ├── category/
    │   ├── email/
    │   ├── event/
    │   ├── organizer/
    │   ├── review/
    │   ├── ticket/
    │   ├── user/
    │   └── vendor/
    │
    ├── repository/               # Spring Data JPA repositories
    │   ├── BookingRepository
    │   ├── EventRepository
    │   ├── JwtBlacklistRepository
    │   ├── RefreshTokenRepository
    │   ├── VendorRepository
    │   └── ... (15 repositories total)
    │
    ├── entity/                   # JPA domain entities
    │   ├── User, Vendor, Event, Booking, BookingItem
    │   ├── Review, Category, TicketCategory
    │   ├── FavoriteEvent, VendorFollow, EventOrganizer
    │   ├── RefreshToken, JwtBlacklist
    │   ├── PasswordResetToken, VerificationToken
    │   └── AuditLog
    │
    ├── dto/                      # Data Transfer Objects
    │   ├── request/              # CreateUserRequest, CreateEventRequest, etc.
    │   └── response/             # UserResponse, EventResponse, etc.
    │
    └── security/                 # Spring Security configuration
        ├── config/SecurityConfig
        ├── jwt/JwtService, JwtFilter, JwtAuthEntryPoint
        └── user/CustomUserDetailsService
```

---

# 🔗 Trello Board: 
## View CrowdGo Project Board

# This board includes:

- Feature planning
- Backend task distribution
- Sprint progress tracking
- Bug fixing workflow
- Future improvements roadmap

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+
- A Gmail account (for the email service)

### 1. Clone the Repository

```bash
git clone https://github.com/Nuray745/multi-vendor-event-management.git
```

### 2. Create the Database

```sql
CREATE DATABASE event_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Environment Variables

Create an `.env` file in the root directory (loaded automatically via `spring-dotenv`):

```env
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

JWT_SECRET=your_base64_encoded_secret_key
JWT_EXPIRATION_SECONDS=3600

MAIL_USERNAME=your_gmail_address@gmail.com
MAIL_PASSWORD=your_gmail_app_password

APP_BASE_URL=http://localhost:8080
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

> **Note:** For Gmail, generate an **App Password** from your Google account (2FA must be enabled).

### 4. Build & Run

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Environment Variables

| Variable | Description | Required |
|---|---|---|
| `DB_USERNAME` | MySQL database username | ✅ |
| `DB_PASSWORD` | MySQL database password | ✅ |
| `JWT_SECRET` | Base64-encoded HMAC secret key (min 256-bit) | ✅ |
| `JWT_EXPIRATION_SECONDS` | Access token lifetime in seconds (e.g. `3600`) | ✅ |
| `MAIL_USERNAME` | Gmail address used to send emails | ✅ |
| `MAIL_PASSWORD` | Gmail App Password (not your account password) | ✅ |
| `APP_BASE_URL` | Base URL for email links (default: `http://localhost:8080`) | ⬜ |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed frontend origins | ⬜ |

---

## API Overview

All endpoints are prefixed with `/api`. Authentication is required on protected routes via `Authorization: Bearer <token>`.

| Domain | Base Path | Access |
|---|---|---|
| Auth | `/api/users/register`, `/api/users/login` | Public |
| Users | `/api/users/**` | Authenticated |
| Vendors | `/api/vendors/**` | Public / Vendor |
| Events | `/api/events/**` | Public / Organizer |
| Bookings | `/api/bookings/**` | Authenticated |
| Reviews | `/api/reviews/**` | Authenticated |
| Categories | `/api/categories/**` | Public / Admin |
| Ticket Categories | `/api/ticket-categories/**` | Organizer |
| Social | `/api/interactions/**` | Authenticated |
| Admin - Users | `/api/admin/users/**` | Admin only |
| Admin - Vendors | `/api/admin/vendors/**` | Admin only |

> 📌 Swagger / OpenAPI documentation is planned for a future release. In the meantime, refer to the controller layer for endpoint details.

---

## Security Design

CrowdGo implements a layered security model:

| Mechanism | Implementation |
|---|---|
| **Authentication** | Stateless JWT via `Authorization: Bearer` header |
| **Access Token** | Short-lived (configurable TTL), signed with HMAC-SHA256 |
| **Refresh Token** | Long-lived, stored in database, rotated on each use |
| **Token Blacklist** | Revoked JWTs stored in `jwt_blacklist` table, checked on every request |
| **Email Verification** | UUID token emailed on registration; account gated until verified |
| **Password Reset** | Expiring single-use token; validated and cleared on use |
| **RBAC** | Method-level access control via Spring Security authorities |
| **CORS** | Configurable allowed origins via environment variable |

---

## Future Improvements

- [ ] **OpenAPI / Swagger UI** — Interactive API documentation via SpringDoc
- [ ] **Pagination & Filtering** — Add pageable queries to events, vendors, and bookings
- [ ] **Payment Integration** — Stripe or PayPal for real ticket purchases
- [ ] **Notification Service** — Real-time booking/event notifications (WebSocket or email)
- [ ] **Image Upload** — Cloudinary or S3 integration for event banners and vendor photos
- [ ] **Docker & Docker Compose** — Containerized deployment with MySQL included
- [ ] **Unit & Integration Tests** — Expanded test coverage with Mockito and Testcontainers
- [ ] **Redis Caching** — Session-level caching for frequently accessed event data
- [ ] **CI/CD Pipeline** — GitHub Actions for automated build, test, and deploy workflows
- [ ] **Soft Delete** — Non-destructive record deletion across all domain entities

---

## 👤 Authors

**[shaig-mahmudov]** 
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/shaig-mahmudov)

**[Nuray745]** 
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Nuray745)

**[IslamSamadov]** 
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/IslamSamadov)


---

<div align="center">

Built with using Java & Spring Boot

</div>
