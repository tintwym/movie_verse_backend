package dev.team08.movie_verse_backend.dto.response;

public class MLReviewResponse {
    private String results;

    // ✅ Default Constructor (Required for JSON deserialization)
    public MLReviewResponse() {}

    // ✅ Constructor
    public MLReviewResponse(String results) {
        this.results = results;
    }

    // ✅ Getter & Setter
    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}

