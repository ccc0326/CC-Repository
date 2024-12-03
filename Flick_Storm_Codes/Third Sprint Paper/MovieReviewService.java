package com.rabbiter.cm.service;
import com.rabbiter.cm.domain.MovieReview;
import com.rabbiter.cm.mapper.MovieReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieReviewService {

    @Autowired
    private MovieReviewMapper movieReviewMapper;

    public void addReview(MovieReview review) {
        movieReviewMapper.insertReview(review);
    }

    public List<MovieReview> getReviewsByMovieId(Long movieId) {
        return movieReviewMapper.findReviewsByMovieId(movieId);
    }
    public Double getMovieAverageRating(Long movieId) {
        return movieReviewMapper.getAverageRating(movieId);
    }

    public int getMovieRatingCount(Long movieId) {
        return movieReviewMapper.getRatingCount(movieId);
    }
}
