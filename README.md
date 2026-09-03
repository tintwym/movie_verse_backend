# Movie Verse Backend

Spring Boot **3.4** REST API for MovieVerse (`Java 17`, Maven). It backs authentication, profiles, movies, ratings, reviews, favorites, watchlists, recommendations, and integrations with external data.

## Requirements

- JDK 17
- Maven
- A [Neon](https://neon.tech) PostgreSQL database

## Configuration

1. Copy `src/main/resources/application.properties.example` to `application.properties` (same directory; gitignored for secrets).
2. From the Neon dashboard → **Connection details**, set:
   - `spring.datasource.url` — JDBC form: `jdbc:postgresql://HOST/DB?sslmode=require`  
     (if Neon shows `postgresql://USER:PASSWORD@HOST/DB?sslmode=require`, drop the credentials from the URL and prefix with `jdbc:`)
   - `spring.datasource.username` / `spring.datasource.password`
   - JWT `jwt.secret` / `jwt.expiration`, and any other values your environment needs

Hibernate `ddl-auto=update` creates/updates tables on first start.

### Deploy on Render

1. New **Web Service** → connect `movie_verse_backend`
2. **Native Java** (recommended):
   - Build: `./mvnw -DskipTests package`
   - Start: `java -Dserver.port=$PORT -jar target/backend-0.0.1-SNAPSHOT.jar`
3. Or **Docker**: use the repo `Dockerfile` (already reads `$PORT`)
4. Set env vars: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, and `cors.allowed-origins` (your live web URL)

The app listens on `server.port=${PORT:8080}` so Render health checks pass.

## Run

```bash
cd backend
./mvnw spring-boot:run
```

On a typical setup the app listens on **port 8080** (see `Dockerfile` / Spring defaults).

## Docker

Requires [Docker](https://docs.docker.com/get-docker/) with Compose. The API container talks to **Neon** (no local database container).

```bash
cd backend
cp .env.example .env   # required — paste Neon JDBC URL, user, password
docker compose up --build
```

| Service | URL / port |
|---------|------------|
| Backend API | `http://localhost:8080` |
| Database | Neon PostgreSQL (from `.env`) |

Useful commands:

```bash
docker compose up --build -d   # detached
docker compose logs -f backend
docker compose down
```

Build/run the image alone:

```bash
docker build -t movie-verse-backend .
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL='jdbc:postgresql://ep-xxxx.region.aws.neon.tech/neondb?sslmode=require' \
  -e SPRING_DATASOURCE_USERNAME=neondb_owner \
  -e SPRING_DATASOURCE_PASSWORD=your-neon-password \
  -e JWT_SECRET=change-me-to-a-long-random-secret-key \
  movie-verse-backend
```

## Tests

```bash
./mvnw test
```

## Layout

- `src/main/java/dev/team08/backend/` — controllers, services, entities, security filters, DTOs
- `src/main/resources/` — properties and static resources

See the workspace root for how this service connects to the web frontend.
