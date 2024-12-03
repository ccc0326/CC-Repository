package com.rabbiter.cm.mapper;
import com.rabbiter.cm.domain.MovieReview;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieReviewMapper {

    @Insert("INSERT INTO movie_reviews (movie_id, user_id, rating, comment, created_at) VALUES (#{movieId}, #{userId}, #{rating}, #{comment}, #{createdAt})")
    void insertReview(MovieReview review);

    @Select("SELECT * FROM movie_reviews WHERE movie_id = #{movieId}")
    List<MovieReview> findReviewsByMovieId(Long movieId);
    // 查询电影的平均评分
    @Select("SELECT AVG(rating) FROM movie_reviews WHERE movie_id = #{movieId}")
    Double getAverageRating(@Param("movieId") Long movieId);

    // 查询电影的评分人数
    @Select("SELECT COUNT(*) FROM movie_reviews WHERE movie_id = #{movieId}")
    int getRatingCount(@Param("movieId") Long movieId);
}
