package dev.team08.movie_verse_backend.dto.request;

import dev.team08.movie_verse_backend.entity.FavoriteCategory;

public class AddFavoriteRequest {
    private String movieTitle; // 只需要传递电影的名称
    private FavoriteCategory category; // 电影分类，如喜欢、稍后观看等

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public FavoriteCategory getCategory() {
        return category;
    }

    public void setCategory(FavoriteCategory category) {
        this.category = category;
    }
}
