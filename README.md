# Job Tracker — AI-Powered Job Application Manager

A full-stack backend system built with Spring Boot that helps job seekers track applications and get AI-powered resume-job description match scoring.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/license-MIT-brightgreen)

## Features

- JWT Authentication — secure register/login with BCrypt password hashing
- Job Application CRUD — track applications with status pipeline (Saved → Applied → Interview → Offer → Rejected)
- Resume Upload — PDF parsing with Apache Tika, extracted text stored for matching
- AI Match Scoring — Gemini API scores resume against job description, returns matched keywords, missing skills, and improvement suggestions
- Dashboard Analytics — pipeline counts, interview rate, average match score, weekly timeline
- Deadline Alerts — scheduled email notifications 24 hours before application deadlines
- Unit Tests — 8 tests with JUnit 5 and Mockito covering auth and application service layers

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT |
| ORM | JPA / Hibernate |
| Database | MySQL 8 |
| PDF Parsing | Apache Tika |
| AI Integration | Google Gemini API |
| Email | JavaMail (Gmail SMTP) |
| Testing | JUnit 5 + Mockito |

## Getting Started

### Prerequisites
- Java 17+
- MySQL 8+ (or Docker)
- Maven

### Option 1 — Run with Docker (easiest)
```bash
docker-compose up -d
```

This starts MySQL automatically.

### Option 2 — Run locally

1. Create MySQL database:
```sql
CREATE DATABASE job_tracker;
```

2. Clone the repo:
```bash
git clone https://github.com/YOUR_USERNAME/job-tracker.git
cd job-tracker
```

3. Configure `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
app.gemini.api-key=YOUR_GEMINI_KEY
spring.mail.username=YOUR_GMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

4. Run:
```bash
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Create account |
| POST | /api/auth/login | Login, returns JWT |

### Applications
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/applications | List all (paginated, filterable by status) |
| POST | /api/applications | Create new application |
| PUT | /api/applications/{id} | Update application |
| DELETE | /api/applications/{id} | Delete application |

### Resume
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/resumes/upload | Upload PDF resume |
| GET | /api/resumes | List all resumes |
| PUT | /api/resumes/{id}/activate | Set active resume |

### AI Match
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/match/score | Score resume vs job description |
| GET | /api/match/history/{applicationId} | Match history |

### Dashboard
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/dashboard/stats | Pipeline counts, rates, weekly timeline |

## Running Tests
```bash
mvn test
```

## Resume Bullet Point

> Built a full-stack Job Application Tracker with AI-powered resume-JD matching (Spring Boot, Gemini API, Apache Tika, MySQL) — automated application pipeline management with deadline alerts and 8-test unit test suite using JUnit 5 and Mockito.

## License

MIT
