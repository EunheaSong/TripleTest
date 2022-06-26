package com.example.tripletest.service;

import com.example.tripletest.ReviewEventDto;
import com.example.tripletest.entity.Members;
import com.example.tripletest.entity.Photo;
import com.example.tripletest.entity.Place;
import com.example.tripletest.entity.Review;
import com.example.tripletest.repository.MembersRepository;
import com.example.tripletest.repository.PhotoRepository;
import com.example.tripletest.repository.PlaceRepository;
import com.example.tripletest.repository.ReviewRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventServiceTest {

    @Autowired
    private MembersRepository membersRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ReviewService reviewService;

    private Place place1 = new Place("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
    private Place place2 = new Place("L.A");
    private Members member1 = new Members("Kim");
    private Members member2 = new Members("Park");

    private Review review1;
    private Review review2;

    @Test
    @Order(1)
    @DisplayName("리뷰 등록")
    void pointEventADD() {
        membersRepository.save(member1);
        membersRepository.save(member2);
        placeRepository.save(place1);
        placeRepository.save(place2);

        List<String> photoList = new ArrayList<>();
        List<String> photoList2 = new ArrayList<>();
        photoList.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
        photoList.add("afb0cef2-851d-4a50-bb07-9cc15cbdc332");

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("좋아요!")
                .attachedPhotoIds(photoList)
                .userId(member1.getUserId())
                .placeId("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .build();
        Review review1 = Review.builder()
                .place(place1)
                .content(reviewEventDto.getContent())
                .reviewId(reviewEventDto.getReviewId())
                .userId(reviewEventDto.getUserId())
                .build();
        int result = eventService.pointEvent(reviewEventDto);

        review1.firstReview();
        review1.addPlace();
        reviewRepository.save(review1);
        placeRepository.save(place1);

        Photo photo1 = Photo.builder()
                .photoId(photoList.get(0))
                .review(review1)
                .build();
        photoRepository.save(photo1);
        photo1.addPhoto();

        Photo photo2 = Photo.builder()
                .photoId(photoList.get(1))
                .review(review1)
                .build();
        photoRepository.save(photo2);
        photo2.addPhoto();

        ReviewEventDto reviewEventDto2 = ReviewEventDto.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review2")
                .content("좋아요!")
                .attachedPhotoIds(photoList2)
                .userId(member2.getUserId())
                .placeId("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .build();
        Review review2 = Review.builder()
                .place(place1)
                .content(reviewEventDto2.getContent())
                .reviewId(reviewEventDto2.getReviewId())
                .userId(reviewEventDto2.getUserId())
                .build();
        review2.firstReview();
        reviewRepository.save(review2);

        int result2 = eventService.pointEvent(reviewEventDto2);

        assertEquals(result, 3);
        assertTrue(review1.isFirst());
        assertEquals(result2, 1);
        assertFalse(review2.isFirst());
        this.review1 = review1;
        this.review2 = review2;
    }

    @Test
    @Order(2)
    @DisplayName("리뷰 수정_사진 제거")
    void pointEventMOD() {
        List<String> photoList = new ArrayList<>();

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("MOD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("별로!")
                .attachedPhotoIds(photoList)
                .userId(member1.getUserId())
                .placeId("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .build();

        int result = eventService.pointEvent(reviewEventDto);
        reviewService.removePhoto(review1);
        reviewService.addPhoto(photoList, review1);

        assertEquals(result, 2);
    }

    @Test
    @Order(3)
    @DisplayName("리뷰 수정_사진 추가")
    void pointEventMOD2() {
        List<String> photoList = new ArrayList<>();
        photoList.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("MOD")
                .reviewId("review2")
                .content("짱이다!")
                .attachedPhotoIds(photoList)
                .userId(member2.getUserId())
                .placeId("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .build();

        int result = eventService.pointEvent(reviewEventDto);

        Photo photo1 = Photo.builder()
                .photoId(photoList.get(0))
                .review(review2)
                .build();
        photoRepository.save(photo1);

        reviewService.removePhoto(review2);
        reviewService.addPhoto(photoList, review2);

        assertEquals(result, 2);
    }

    @Test
    @Order(4)
    @DisplayName("리뷰 삭제")
    void pointEventDELETE() {
        List<String> photoList = new ArrayList<>();

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("DELETE")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("별로!")
                .attachedPhotoIds(photoList)
                .userId(member1.getUserId())
                .placeId("2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .build();
        int result = eventService.pointEvent(reviewEventDto);

        assertEquals(result, 0);

    }
}