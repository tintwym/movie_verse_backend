package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
//
//    Optional<Movie> findByTitle(String title);
//    List<Movie> findByIsDeletedFalse();
//    void deleteByTitle(String title);
//	Page<Movie> findAll(Pageable pageable);
//    // ✅ Find movies sorted by rating (highest first)
//    Page<Movie> findByOrderByRatingDesc(Pageable pageable);
//    
//    // ✅ Find upcoming movies (release year in the future)
//	Page<Movie> findByReleaseYearGreaterThanEqualOrderByReleaseYearAsc(int year, Pageable pageable);
//
//    Page<Movie> findByIsDeletedFalse(Pageable pageable);
//    List<Movie> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);
//    List<Movie> findByIsDeletedFalseOrderByTitleAsc();	
//    List<Movie> findByIsDeletedFalseOrderByViewsDesc();
//    //List<Movie> findByIsDeletedFalseOrderByRatingCountDesc();
//    
//    @Query("SELECT m FROM Movie m WHERE m.id IN :movieIds")
//    Page<Movie> findMoviesByIdList(@Param("movieIds") List<UUID> movieIds, Pageable pageable);
//    
//    @Query("""
//            SELECT m FROM Movie m 
//            JOIN m.genres g 
//            WHERE g.name = :genre 
//            ORDER BY m.views DESC
//        """)
//    Page<Movie> findPopularMoviesByGenre(@Param("genre") String genre, Pageable pageable);
//
//    // 1️⃣ 按电影名称（模糊匹配）
//    List<Movie> findByTitleContainingIgnoreCase(String title);
//
//    // 2️⃣ 按类型搜索
//    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.name = :genreName")
//    List<Movie> findByGenreName(@Param("genreName") String genreName);
//
//    // 3️⃣ 按年份搜索
//    List<Movie> findByReleaseYear(int year);
//
//    // 4️⃣ 组合搜索：按标题、类型、年份（可选参数）
//    @Query("SELECT m FROM Movie m " +
//            "LEFT JOIN m.genres g " +
//            "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
//            "AND (:genre IS NULL OR g.name = :genre) " +
//            "AND (:year IS NULL OR m.releaseYear = :year)")
//    List<Movie> searchMovies(
//            @Param("title") String title,
//            @Param("genre") String genre,
//            @Param("year") Integer year
//    );
//
//    // 4️⃣ 按评分范围搜索
//    @Query("SELECT m FROM Movie m WHERE m.rating BETWEEN :minRating AND :maxRating")
//    List<Movie> findByRatingBetween(@Param("minRating") double minRating, @Param("maxRating") double maxRating);
//
//    // 5️⃣ 按导演搜索
//    @Query("SELECT m FROM Movie m JOIN m.directors d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<Movie> findByDirectorName(@Param("name") String name);
//
//    // 6️⃣ 按演员搜索
//    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<Movie> findByActorName(@Param("name") String name);
//
//    // 7️⃣ 按国家搜索
//    @Query("SELECT m FROM Movie m JOIN m.countries c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :country, '%'))")
//    List<Movie> findByCountryName(@Param("country") String country);
//
//    // 8️⃣ 按制片公司搜索
//    @Query("SELECT m FROM Movie m JOIN m.producers p WHERE LOWER(p.companyName) LIKE LOWER(CONCAT('%', :company, '%'))")
//    List<Movie> findByProducerName(@Param("company") String company);
//
//    // 9️⃣ **多条件综合查询**
//    @Query("SELECT m FROM Movie m " +
//            "LEFT JOIN m.genres g " +
//            "LEFT JOIN m.directors d " +
//            "LEFT JOIN m.actors a " +
//            "LEFT JOIN m.countries c " +
//            "LEFT JOIN m.producers p " +
//            "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
//            "AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%'))) " +
//            "AND (:year IS NULL OR m.releaseYear = :year) " +
//            "AND (:minRating IS NULL OR m.rating >= :minRating) " +
//            "AND (:maxRating IS NULL OR m.rating <= :maxRating) " +
//            "AND (:director IS NULL OR LOWER(d.firstName) LIKE LOWER(CONCAT('%', :director, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :director, '%'))) " +
//            "AND (:actor IS NULL OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :actor, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :actor, '%'))) " +
//            "AND (:country IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :country, '%'))) " +
//            "AND (:company IS NULL OR LOWER(p.companyName) LIKE LOWER(CONCAT('%', :company, '%')))")
//    List<Movie> searchMovies(@Param("title") String title,
//                             @Param("genre") String genre,
//                             @Param("year") Integer year,
//                             @Param("minRating") Double minRating,
//                             @Param("maxRating") Double maxRating,
//                             @Param("director") String director,
//                             @Param("actor") String actor,
//                             @Param("country") String country,
//                             @Param("company") String company);
//
//
//    // 1️⃣ **按照观看人数降序排序**
//    @Query("SELECT m FROM Movie m ORDER BY m.views DESC")
//    List<Movie> findMoviesOrderByViewsDesc(Pageable pageable);
//
//    // 2️⃣ **按照评分降序排序**
//    @Query("SELECT m FROM Movie m ORDER BY m.rating DESC")
//    List<Movie> findMoviesOrderByRatingDesc(Pageable pageable);
//    
//    @Query("SELECT m FROM Movie m JOIN m.keywords k WHERE k.name LIKE %:keyword%")
//    List<Movie> findByKeyword(@Param("keyword") String keyword);
//    
}
