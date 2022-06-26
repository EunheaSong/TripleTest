package com.example.tripletest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(indexes = @Index(name = "i_member", columnList = "userId"))
public class Members extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @OneToMany(mappedBy = "member")
    private List<Mileage> mileage = new ArrayList<>();

    private int totalPoint = 0;

    public Members (String userId){
        this.userId = userId;
    }

    public void editPoint (int num){
        this.totalPoint += num;
    }

}
