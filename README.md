# Movie Verse Backend

Spring Boot **3.4** REST API for MovieVerse (`Java 17`, Maven). It backs authentication, profiles, movies, ratings, reviews, favorites, watchlists, recommendations, and integrations with external data.

## Requirements

- JDK 17
- Maven
- MySQL 8 (schema URL and credentials in your local config)

## Configuration

1. Copy `src/main/resources/application.properties.example` to `application.properties` (same directory, and ensure it is gitignored for secrets).
2. Set MySQL `spring.datasource.*`, JWT `jwt.secret` / `jwt.expiration`, and any other values your environment needs.

## Run

```bash
cd movie_verse_backend
./mvnw spring-boot:run
```

On a typical setup the app listens on **port 8080** (see `Dockerfile` / Spring defaults). The Android app in `movie_verse_mobile` is preconfigured for `http://10.0.2.2:8080/` when using the emulator.

## ML review service

Review flows can call a Python prediction service. `MovieReviewService` posts to a URL such as `http://127.0.0.1:5001/predict` — align that port and path with your running Flask app in `movie_verse_ml` (default there is often **5000** unless you change it).

## Docker

Build and run with the provided `Dockerfile` (image exposes **8080**).

## Tests

```bash
./mvnw test
```

## Layout

- `src/main/java/dev/team08/movie_verse_backend/` — controllers, services, entities, security filters, DTOs
- `src/main/resources/` — properties and static resources

See the workspace root `README.md` for how this service connects to the web app, mobile client, and ML service.
