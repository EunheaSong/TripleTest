package com.example.tripletest.controller;



import com.example.tripletest.entity.Members;
import com.example.tripletest.entity.Photo;
import com.example.tripletest.entity.Place;
import com.example.tripletest.entity.Review;
import com.example.tripletest.repository.MembersRepository;
import com.example.tripletest.repository.PlaceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//실제 어플리케이션을 구동할때처럼 모든 bean을 컨테이너에 등록함.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MembersRepository membersRepository;
    @Autowired
    private PlaceRepository placeRepository;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private Members member1 = new Members("Kim");
    private Members member2 = new Members("Park");

    private Place place1 = new Place("프랑스");
    private Place place2 = new Place("L.A");

    private List<String> photo = new ArrayList<>();

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    @Test
    @Order(1)
    @DisplayName("리뷰 첫 등록")
    void firstReviewTest() throws JsonProcessingException {

        //give
        membersRepository.save(member1);
        membersRepository.save(member2);
        placeRepository.save(place1);
        placeRepository.save(place2);

        photo.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
        photo.add("afb0cef2-851d-4a50-bb07-9cc15cbdc332");

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("좋아요!")
                .attachedPhotoIds(photo)
                .userId(member1.getUserId())
                .placeId("프랑스")
                .build();

        String requestBody = mapper.writeValueAsString(reviewEventDto);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/events",
                request,
                String.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String reviewEventResponse = response.getBody();
        assertNotNull(reviewEventResponse);
        assertEquals("SUCCESS", reviewEventResponse);
    }

    @Test
    @Order(2)
    @DisplayName("포인트 조회")
    void getMileageTest() {
        // when
        //1.멤버1의 마일리지 조회
        ResponseEntity<Integer> response = restTemplate.getForEntity(
                "/points/" + member1.getId().toString(), Integer.class);
        //2.멤버2의 마일리지 조회
        ResponseEntity<Integer> response2 = restTemplate.getForEntity(
                "/points/" + member2.getId().toString(), Integer.class);

        // then
        //1.멤버1의 마일리지 조회
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer totalPoint = response.getBody();
        assertNotNull(response);
        assertEquals(3, totalPoint);

        //2.멤버2의 마일리지 조회
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Integer totalPoint2 = response2.getBody();
        assertNotNull(response);
        assertEquals(0, totalPoint2);
    }

    @Test
    @Order(3)
    @DisplayName("리뷰 등록")
    void creatReviewTest() throws JsonProcessingException {
        //give
        List<Photo> photoList = new ArrayList<>();
        List<String> photo = new ArrayList<>();

        ReviewEventDto reviewEventDto = ReviewEventDto.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review1")
                .content("좋아요!")
                .attachedPhotoIds(photo)
                .userId(member1.getUserId())
                .placeId("프랑스")
                .build();

        ReviewEventDto reviewEventDto2 = ReviewEventDto.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("review2")
                .content("좋아요!")
                .attachedPhotoIds(photo)
                .userId(member2.getUserId())
                .placeId("프랑스")
                .build();

        Review review1 = Review.builder()
                .reviewId(reviewEventDto.reviewId)
                .userId(reviewEventDto.userId)
                .content(reviewEventDto.content)
                .place(place1)
                .build();
        review1.firstReview();

        Review review2 = Review.builder()
                .reviewId("review2")
                .userId("Kim")
                .content("좋아요!")
                .place(place2)
                .build();
        Photo photo1 = Photo.builder()
                .review(review2)
                .photoId("사진1")
                .build();
        photoList.add(photo1);
        Photo photo2 = Photo.builder()
                .review(review2)
                .photoId("사진2")
                .build();
        photoList.add(photo2);
        review2.firstReview();

        //when
        String requestBody = mapper.writeValueAsString(reviewEventDto);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/events",
                request,
                String.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String reviewEventResponse = response.getBody();
        assertNotNull(reviewEventResponse);
        assertEquals("SUCCESS", reviewEventResponse);

    }


    @Builder
    @Getter
    static class ReviewDto {

        private String reviewId;
        private String content;
        private List<String> attachedPhotoIds;
        private String userId;
        private String placeId;

    }

    @Builder
    @Getter
    static class ReviewEventDto {

        private String type;
        private String action;
        private String reviewId;
        private String content;
        private List<String> attachedPhotoIds;
        private String userId;
        private String placeId;

    }

}