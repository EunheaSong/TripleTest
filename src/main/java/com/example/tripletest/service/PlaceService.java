package com.example.tripletest.service;

import com.example.tripletest.entity.Place;
import com.example.tripletest.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public boolean checkFirstReview(String placeId){
        Place place = findPlace(placeId);
        System.out.println("안비어 있잖아.." +place.getReviewList().size() + "비었어? "+ place.getReviewList().isEmpty());
        return place.getReviewList().isEmpty();
    }


    public Place findPlace (String placeId){
        return placeRepository.findByPlaceId(placeId).orElseThrow(
                ()-> new NullPointerException("일치하는 장소가 존재하지 않습니다.")
        );
    }

}