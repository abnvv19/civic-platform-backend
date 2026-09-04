# 🏙️ Civic Platform Backend

A backend application for a **Civic Problem Reporting Platform** where citizens can report civic issues, upload images, track their problems, and receive AI-powered analysis.

The platform uses **Spring Boot, Spring Security, JWT Authentication, MySQL, and Google Gemini AI** to provide intelligent civic problem management.

---

## 🚀 Features

### 👤 User Management & Authentication

- 👤 User Registration
- 🔐 User Login
- 🔑 JWT-based Authentication
- 🔒 Password Encryption using BCrypt
- 🛡️ Protected API Endpoints
- 👥 Role-based Authorization
- 🔐 Spring Security Integration

---

### 🏙️ Civic Problem Management

Users can report and manage civic problems such as:

- 🛣️ Road Damage
- 🗑️ Garbage Issues
- 💡 Streetlight Problems
- 💧 Water Leakage
- 🧹 Sanitation Issues
- ⚡ Electricity Shortage
- 🌾 Agriculture Problems
- 🏥 Healthcare Issues
- 📌 Other Civic Problems

Users can:

- Create civic problem reports
- Add a title and description
- Select a category
- Provide address and city information
- Upload an image
- View all civic problems
- View a specific problem
- View their own reported problems
- Track problem status

---

## 🤖 AI-Powered Features

The platform integrates **Google Gemini AI** to provide intelligent analysis of civic problems.

### 🧠 AI Problem Analysis

When a user reports a civic problem, AI analyzes the report and determines:

- 📂 Suggested Problem Category
- 🚨 Problem Severity
- 🖼️ Image Verification
- 📊 AI Confidence Score

### 🔍 AI Duplicate Detection

Before creating a new civic problem, the system checks existing problems in the same city.

AI analyzes:

- Title similarity
- Description similarity
- Category
- Address and location

The system returns:

- Whether the problem is a potential duplicate
- Matching Problem ID
- Duplicate Confidence Score
- Explanation for the duplicate detection

---

## 🔄 Problem Lifecycle


OPEN
  ↓
IN_PROGRESS
  ↓
RESOLVED

Admins can manage and update the status of reported civic problems.

🏗️ Application Flow
Citizen
   │
   ▼
Register / Login
   │
   ▼
JWT Authentication
   │
   ▼
Submit Civic Problem
   │
   ├───────────────► 🔍 AI Duplicate Detection
   │
   ├───────────────► 🤖 AI Problem Analysis
   │                     │
   │                     ├── Suggested Category
   │                     ├── Severity
   │                     ├── Image Verification
   │                     └── Confidence Score
   │
   ▼
Image Storage
   │
   ▼
MySQL Database
   │
   ▼
Return Problem Response
🛠️ Tech Stack
Backend
Java
Spring Boot
Spring Web MVC
Spring Security
Spring Data JPA
Hibernate
Database
MySQL
Authentication
JWT (JSON Web Token)
BCrypt Password Encryption
AI
Google Gemini API
Gemini Flash Model
Other Tools
Maven
Lombok
Jackson
REST APIs
📁 Project Structure
src/main/java/in/abnv/civic_platform_backend
│
├── ai
│   ├── dto
│   │   ├── AiAnalysisResponseDto.java
│   │   └── DuplicateProblemAnalysisDto.java
│   │
│   ├── model
│   │   └── Severity.java
│   │
│   └── service
│       └── GeminiService.java
│
├── config
│   └── SecurityConfig.java
│
├── civicproblems
│   │
│   ├── controller
│   │   └── CivicProblemController.java
│   │
│   ├── dto
│   │   ├── CreateCivicProblemRequestDto.java
│   │   └── CivicProblemResponseDto.java
│   │
│   ├── entity
│   │   ├── CivicProblem.java
│   │   ├── ProblemCategory.java
│   │   └── ProblemStatus.java
│   │
│   ├── repository
│   │   └── CivicProblemRepository.java
│   │
│   └── service
│       └── CivicProblemService.java
│
├── jwt
│   ├── JwtAuthenticationFilter.java
│   └── JwtService.java
│
├── storage
│   └── FileStorageService.java
│
├── users
│   │
│   ├── controller
│   │   └── UserController.java
│   │
│   ├── dto
│   │   ├── RegisterUserRequestDto.java
│   │   ├── LoginRequestDto.java
│   │   └── LoginResponseDto.java
│   │
│   ├── entity
│   │   ├── User.java
│   │   └── Role.java
│   │
│   ├── repository
│   │   └── UserRepository.java
│   │
│   └── service
│       └── UserService.java
│
└── CivicPlatformBackendApplication.java
🔐 Authentication

The application uses JWT-based authentication.

Authentication Flow
User Registration
       ↓
Password encrypted using BCrypt
       ↓
User Login
       ↓
JWT Token Generated
       ↓
Client sends JWT Token
       ↓
JWT Authentication Filter
       ↓
Protected API Access

Protected requests require the JWT token in the request header:

Authorization: Bearer <your-jwt-token>
🤖 AI Analysis Example

When a user submits a civic problem, the AI can return information like:

{
  "suggestedCategory": "ROAD_DAMAGE",
  "severity": "HIGH",
  "imageVerified": true,
  "confidence": 94.0
}
🔍 Duplicate Detection Example

If a similar problem already exists, the AI can return:

{
  "duplicate": true,
  "duplicateProblemId": 12,
  "confidence": 92.0,
  "reason": "The report describes a similar civic issue at the same location."
}
📊 Example Civic Problem Response
{
  "id": 1,
  "title": "Large pothole on MG Road",
  "description": "A large pothole is causing problems for vehicles.",
  "category": "ROAD_DAMAGE",
  "status": "OPEN",
  "address": "MG Road",
  "city": "Ghaziabad",

  "aiSuggestedCategory": "ROAD_DAMAGE",
  "severity": "HIGH",
  "imageVerified": true,
  "aiConfidence": 94.0,

  "duplicate": false,
  "duplicateProblemId": null,
  "duplicateConfidence": 0.0,
  "duplicateReason": "No similar problem found"
}
⚙️ Configuration

Configure your database and API key in:

src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:mysql://localhost:3306/civic_platform
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

gemini.api.key=YOUR_GEMINI_API_KEY

⚠️ Never commit your database password or Gemini API key to GitHub.

Use environment variables or a separate configuration file for sensitive credentials.

🚀 Future Improvements

The project is actively evolving. Planned features include:

👨‍💼 Admin Dashboard
🔄 Admin Problem Status Management
👍 Community Upvotes
💬 Comments and Discussions
🔔 Notifications
📊 Analytics Dashboard
🗺️ Interactive Problem Map
🤖 AI-Powered Civic Insights
⭐ Citizen Reputation System
📈 Smart Problem Prioritization
🎯 Project Goal

The goal of this project is to build a modern platform that makes civic problem reporting more efficient.

Instead of simply collecting complaints, the platform uses AI-powered analysis and duplicate detection to help organize, classify, and manage civic issues more intelligently.

👨‍💻 Author

Abnvv

Built using Java, Spring Boot, Spring Security, MySQL, JWT, and Google Gemini AI.

⭐ If you found this project interesting, consider giving the repository a star!


