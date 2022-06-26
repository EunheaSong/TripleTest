package com.example.tripletest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(indexes = @Index(name = "i_place", columnList = "placeId"))
public class Place {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String placeId;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Review> reviewList = new ArrayList<>();

    public Place(String placeId){
        this.placeId =placeId;
    }

}
