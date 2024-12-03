package com.rabbiter.cm.controller;
import com.rabbiter.cm.domain.MovieReview;
import com.rabbiter.cm.service.MovieReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class MovieReviewController {

    @Autowired
    private MovieReviewService movieReviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody MovieReview review) {
        movieReviewService.addReview(review);
        return ResponseEntity.ok("提交成功");
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<MovieReview>> getReviews(@PathVariable Long movieId) {
        List<MovieReview> reviews = movieReviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }
    @GetMapping("/average-rating/{movieId}")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long movieId) {
        Double averageRating = movieReviewService.getMovieAverageRating(movieId);
        int ratingCount = movieReviewService.getMovieRatingCount(movieId);

        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", averageRating);
        response.put("ratingCount", ratingCount);

        return ResponseEntity.ok(response);
    }


}
