package com.example.tripletest.service;


import com.example.tripletest.ReviewEventDto;
import com.example.tripletest.entity.Members;
import com.example.tripletest.entity.Mileage;
import com.example.tripletest.entity.Review;
import com.example.tripletest.entity.TypeEnum;
import com.example.tripletest.repository.MembersRepository;
import com.example.tripletest.repository.MileageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final PlaceService placeService;
    private final MembersRepository membersRepository;
    private final MileageRepository mileageRepository;

    public void pointLog (Mileage point){
        log.info(point.getPlace() + "에 "+ point.getType() + " 으로 인해 마일리지가 " + point.getPoint() + "점" + point.getStatus() + "되었습니다.");
    }

    @Transactional
    public int pointEvent (ReviewEventDto dto){
        int point = 0;
        switch (dto.getAction()) {
            case "ADD": point = pointAdd(dto);
                break;

            case "MOD": point = pointMod(dto);
                break;

            case "DELETE": point = pointRemove(dto);
                break;
            default:
                throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return point;
    }

    @Transactional
    public int pointAdd (ReviewEventDto dto){
        /*
        텍스트와 사진 여부 확인.  / 해당 장소에 대한 첫리뷰인지 확인.
        텍스트 +1 , 사진 +1 , 첫 리뷰 +1
         */

        //1.회원을 찾는다.
        Members member = memberService.findMember(dto.getUserId());

        //첫 리뷰 보너스 +1
        //장소에 리뷰가 있는지 없는지 확인해야함.
        if(placeService.checkFirstReview(dto.getPlaceId())){
            Mileage point = new Mileage(member, TypeEnum.FIRST, dto.getPlaceId());
            mileageRepository.save(point);
        }
        //텍스트 +1
        if(!dto.getContent().isEmpty()){
            Mileage point = new Mileage(member, TypeEnum.ADDREVIEW, dto.getPlaceId());
            mileageRepository.save(point);
        } else {
            throw new IllegalArgumentException("Null Contents.");
        }
        //사진 +1
        if(!dto.getAttachedPhotoIds().isEmpty()){
            Mileage point = new Mileage(member, TypeEnum.ADDPHOTO, dto.getPlaceId());
            mileageRepository.save(point);
        }
        membersRepository.save(member);
        return member.getTotalPoint();
    }

    @Transactional
    public int pointRemove(ReviewEventDto dto) {
        //포인트 차감
        //사진을 지웠는지 ? 후기를 삭제했는지 ? 확인.
        Members member = memberService.findMember(dto.getUserId());
        Review review = reviewService.findReview(dto.getReviewId());
        //첫리뷰였는지도 확인해야함.
        if (review.isFirst()) {
            Mileage point = new Mileage(member, TypeEnum.DELETEFIRST, dto.getPlaceId());
            mileageRepository.save(point);
        }
        if (!review.getPhotos().isEmpty()) {
            Mileage point = new Mileage(member, TypeEnum.DELETEPHOTO, dto.getPlaceId());
            mileageRepository.save(point);
        }
        Mileage point = new Mileage(member, TypeEnum.DELETEREVIEW, dto.getPlaceId());
        mileageRepository.save(point);

        return member.getTotalPoint();
    }

    @Transactional
    public int pointMod(ReviewEventDto dto){
        //텍스트가 없으면 안된다.
        if(dto.getContent().isEmpty()){
            throw new IllegalArgumentException("Null Contents");
        }
        Members member = memberService.findMember(dto.getUserId());
        Review review = reviewService.findReview(dto.getReviewId());
        if (review.getPhotos().isEmpty()){
            if(!dto.getAttachedPhotoIds().isEmpty()){
                Mileage point = new Mileage(member, TypeEnum.ADDPHOTO, dto.getPlaceId());
                mileageRepository.save(point);
            }
        } else {
            if(dto.getAttachedPhotoIds().isEmpty()){
                Mileage point = new Mileage(member, TypeEnum.DELETEPHOTO, dto.getPlaceId());
                mileageRepository.save(point);
            }
        }
        return member.getTotalPoint();
    }

}