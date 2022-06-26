package com.example.tripletest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@NoArgsConstructor
@Getter
@Entity
public class Mileage extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER")
    private Members member;

    private int point = 0;

    @Enumerated(value = EnumType.STRING)
    private StatusEnum status; //적립, 차감

    @Enumerated(value = EnumType.STRING)
    private TypeEnum type; //첫 리뷰인지, 그냥 리뷰등록인지, 사진등록인지

    private String place; //어떤 장소인지

    public void editPoint () {
        this.point += this.status.point();
        this.member.editPoint(this.status.point());
        System.out.println("지금 적립되는 포인트 " + this.status.point());
    }

    public Mileage (Members member, TypeEnum type, String place) {
        this.member = member;
        this.place = place;
        this.type = type;
        if (type.equals(TypeEnum.FIRST)||type.equals(TypeEnum.ADDREVIEW)||type.equals(TypeEnum.ADDPHOTO)){
            this.status = StatusEnum.ADD;
        } else if(type.equals(TypeEnum.DELETEPHOTO)||type.equals(TypeEnum.DELETEREVIEW)||type.equals(TypeEnum.DELETEFIRST)) {
            this.status = StatusEnum.MINUS;
        }
        editPoint();
        log.info(place + "에 "+ type.status() + " 으로 인해 마일리지가 " + this.status.point() + "점" + this.status.status() + "되었습니다.");
    }

}