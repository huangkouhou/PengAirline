# ✈️ PengAirline
**Full-Stack Flight Booking & Management System**

![CI/CD Status](https://img.shields.io/badge/build-passing-brightgreen)
![Docker](https://img.shields.io/badge/container-Docker-blue)
![Cloud](https://img.shields.io/badge/deployment-Oracle_Cloud-orange)

🌐 **Live Demo:** https://pengairline.penghuang.dev

---

## 📖 Overview

**PengAirline** is a production-ready, full-stack airline reservation and management system that supports real-time flight discovery, ticket booking, user profiles, and comprehensive admin management.

The project is designed with real-world architecture in mind, featuring a modern glassmorphism UI, containerization, role-based access control, and cloud deployment on OCI.

---

## ✨ Key Highlights

- ☁️ **Cloud Deployment** Deployed on Oracle Cloud.

- 🐳 **Full Containerization** Frontend, backend, and web server are fully containerized and orchestrated via **Docker Compose**.

- 🌐 **Nginx Reverse Proxy & API Gateway**
  - HTTPS termination (Let’s Encrypt / Certbot)
  - `/` → React Frontend routing, `/api` → Spring Boot Backend routing
  - Clean CORS handling and improved performance

- 🔄 **CI/CD Pipeline**
  - Automated build and deployment process
  - Builds and pushes Docker images
  - Seamless deployment to OCI environment

- 🔐 **Enterprise-grade Security**
  - Secure User Authentication and Registration
  - JWT-based **Role-Based Access Control (RBAC)**
  - Admin roles and protected routes enforced at the API level

---

## 🛠️ Tech Stack

### Frontend
- React.js (Hooks, React Router v6)
- `react-select` (Advanced searchable dropdowns)
- Custom CSS3 (Modern Glassmorphism UI & Responsive Design)

### Backend
- Java & Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA & Hibernate
- RESTful API Architecture

### Infrastructure & DevOps
- Docker & Docker Compose
- Nginx (Reverse Proxy)
- MySQL (Database)
- Oracle Cloud (Computing) 
- Certbot (HTTPS / SSL)

---

## 👤 User Features

### 🔍 Flight Browsing & Search
- Dual-way airport search with smart `react-select` dropdowns
- Filter flights by departure, arrival, and dates
- High-contrast, user-friendly booking interface

### 🎫 Booking Management
- Book available flights securely
- View real-time flight duration, status, and pricing
- Manage personal flight itineraries

### 🕒 User Dashboard
- Review past and upcoming flight bookings
- Clear status indicators (Scheduled, Delayed, Cancelled)
- Manage user profile and personal information

---

## 🛡️ Admin Features

### 🔐 Secure Admin Access
- Admin-only protected routes and dashboards
- JWT role claims enforced securely at the backend
- Strict RBAC implementation for airline staff

### ✈️ Flight & Route Management
- Add new flights and set base prices
- Update real-time flight statuses
- Manage global IATA airport codes and city data

### 📋 Booking Moderation
- View and manage all passenger bookings
- Process booking statuses (Confirm, Cancel)
- Real-time synchronization with the database

---

## 📸 Screenshots

1️⃣ **Landing Page & Smart Search**
<img width="1601" height="918" alt="截屏2026-03-03 14 23 55" src="https://github.com/user-attachments/assets/3f1f69fb-117a-4e60-938f-ccedefe2ac34" />


2️⃣ **Flight Results & UI**
<img width="1774" height="886" alt="截屏2026-03-03 14 26 36" src="https://github.com/user-attachments/assets/15fba8b7-bf74-46d8-bd45-183ec10ab386" />


3️⃣ **Secure Admin Dashboard**
<img width="1526" height="892" alt="截屏2026-03-03 14 27 40" src="https://github.com/user-attachments/assets/377b8c34-46ef-4623-87e2-d95a5e08fb4b" />


---

## 🚀 Getting Started (Docker)

This project runs entirely with Docker Compose.

### 📦 Prerequisites
- Docker (v20+)
- Docker Compose (v2+)
- Domain name (for HTTPS in production)
- Optional: Let’s Encrypt certificates

### 🔐 Environment Variables

Create a `.env` file in the root directory:

```env
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/pengairline
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password

# JWT Security
JWT_SECRET_KEY=your_super_secret_jwt_key_here
JWT_EXPIRATION_TIME=86400000
