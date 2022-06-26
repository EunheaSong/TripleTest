package com.example.tripletest.service;


import com.example.tripletest.entity.Photo;
import com.example.tripletest.entity.Review;
import com.example.tripletest.repository.PhotoRepository;
import com.example.tripletest.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PhotoRepository photoRepository;

    public Review findReview(String reviewId) {
        return reviewRepository.findByReviewId(reviewId).orElseThrow(
                ()-> new NullPointerException("Not Found Review.")
        );
    }

    public void removePhoto(Review review){
        List<Photo> photoList = photoRepository.findAllByReview(review);
        photoRepository.deleteAll(photoList);
        review.getPhotos().clear();
    }

    public void addPhoto(List<String> photoName, Review review){
        for (String p : photoName){
            Photo photo = Photo.builder()
                    .photoId(p)
                    .review(review)
                    .build();
            photo.addPhoto();
        }
    }


}
