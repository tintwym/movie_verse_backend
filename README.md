# Movie Verse Backend

Spring Boot 3 REST API for Movie Verse (auth, favorites, watchlist, reviews, ratings).

## Prerequisites

- Java 17+
- Maven Wrapper (`./mvnw`) included
- A [Neon](https://neon.tech) PostgreSQL database

## Local run

1. Copy local overrides and fill Neon credentials:

```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

2. Start with the `local` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

API: `http://localhost:8080` · Health: `http://localhost:8080/health`

Hibernate `ddl-auto=update` creates/updates tables on first start.

## Deploy on Render

**Why deploys failed before:** `application.properties` was gitignored, so Render built a JAR with no DB/port config and crashed before opening a port.

### Fix (Docker — recommended)

1. New Web Service → this repo → **Docker**
2. Branch: `development_v1` (or `main` after you merge)
3. Set **Environment** variables:

| Key | Example |
|-----|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://ep-xxx.region.aws.neon.tech/neondb?sslmode=require` |
| `SPRING_DATASOURCE_USERNAME` | `neondb_owner` |
| `SPRING_DATASOURCE_PASSWORD` | your Neon password |
| `JWT_SECRET` | long random string (32+ chars, **required**) |
| `CORS_ALLOWED_ORIGINS` | `https://your-web.vercel.app` |

Optional: `SEED_DEFAULT_ADMIN=true` and `ADMIN_PASSWORD=...` only for demos (off by default).

4. Deploy. Health check: `/health`

### Native Java (alternative)

- Build: `./mvnw -DskipTests package`
- Start: `java -Dserver.port=$PORT -jar target/backend-0.0.1-SNAPSHOT.jar`
- Same env vars as above

## Docker Compose (local)

```bash
cp .env.example .env   # fill SPRING_DATASOURCE_*
docker compose up --build
```

## License

ISS GDipSA 50 Team 8
