# StreamForge

A full-stack video streaming platform built with Spring Boot 4, MySQL, and React — supporting efficient video playback via HTTP byte-range requests instead of full-file downloads.

## Features
- Video upload with metadata storage (MySQL)
- HTTP byte-range streaming (206 Partial Content) — browsers can seek/scrub videos without downloading the entire file
- Paginated video listing
- Interactive API documentation via Swagger/OpenAPI
- Load tested with k6 — 200 concurrent virtual users, 100% success rate, 95th-percentile response time of 4.46ms

## Tech Stack
- **Backend:** Java 21, Spring Boot 4, Spring Data JPA, Hibernate
- **Database:** MySQL 8
- **Frontend:** React.js
- **API Docs:** springdoc-openapi (Swagger UI)
- **Load Testing:** k6

## Getting Started

### Prerequisites
- Java 21+
- MySQL 8+
- Node.js (for frontend)

### Backend Setup
1. Create MySQL database: `CREATE DATABASE streamforge;`
2. Update `src/main/resources/application.properties` with your DB credentials
3. Run: `mvn spring-boot:run`
4. API available at `http://localhost:8080`
5. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Frontend Setup
1. `cd frontend`
2. `npm install`
3. `npm start`
4. App available at `http://localhost:3000`

## Database Design
- **Videos** table stores core metadata
- **WatchProgress** — one row per (user, video) pair, enforced by a unique constraint, updated via an upsert pattern to power resume-playback (similar to Netflix's "continue watching")
- **Comments** — one-to-many relationship to Videos via a foreign key, demonstrating standard relational join queries

### API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/videos/upload` | Upload a video with title/description |
| GET | `/api/videos?page=&size=` | List videos (paginated) |
| GET | `/api/videos/{id}/stream` | Stream video with byte-range support |

## How byte-range streaming works
The `/stream` endpoint reads the `Range` HTTP header and returns only the requested byte slice with a `206 Partial Content` response, enabling video seeking without downloading the full file — the same mechanism used by production video platforms.

## Load Testing
Load tested using [k6](https://k6.io) — see `load-testing/loadtest.js`.

**Run it yourself:**
```bash
k6 run load-testing/loadtest.js
```

**Results:** 200 concurrent virtual users, 100% success rate across 15,582 requests, 95th-percentile response time of 4.46ms, ~256 req/s throughput.
