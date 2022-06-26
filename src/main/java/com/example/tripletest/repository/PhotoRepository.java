package com.example.tripletest.repository;


import com.example.tripletest.entity.Photo;
import com.example.tripletest.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByReview (Review review);
}
