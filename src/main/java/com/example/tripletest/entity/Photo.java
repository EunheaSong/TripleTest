package com.example.tripletest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Photo extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String photoId;

    @ManyToOne
    @JoinColumn(name = "REVIEW")
    private Review review;

    public void addPhoto(){
        this.review.getPhotos().add(this);
    }
}
