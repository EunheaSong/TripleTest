package com.example.tripletest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(indexes = @Index(name = "i_review", columnList = "reviewId"))
public class Review extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(unique = true)
    private String reviewId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String content;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "review")
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "PLACE")
    private Place place;

    private boolean first = false; //첫등록이면 true.

    public void firstReview (){
        if(this.place.getReviewList().isEmpty()){
            this.first = true;
        }
    }
    public void addPlace(){
        this.place.getReviewList().add(this);
    }

}
