package com.example.tripletest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReviewDto {

    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

}
