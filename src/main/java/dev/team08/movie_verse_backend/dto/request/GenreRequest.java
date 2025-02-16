package dev.team08.movie_verse_backend.dto.request;

import java.util.UUID;

public class GenreRequest {
    private UUID id;
    private String name;

    // 构造函数
    public GenreRequest(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public GenreRequest(String name){
        this.name = name;
    }

    public GenreRequest() {
    }

    // Getter 和 Setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
