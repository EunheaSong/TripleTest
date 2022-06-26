package com.example.tripletest.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // 변수가 전부 piravte 임으로 GET이 필요하다.
@MappedSuperclass // 상속했을 때, 컬럼으로 인식하게 합니다.컬럼=열
@EntityListeners(AuditingEntityListener.class) // 생성/수정 시간을 자동으로 반영하도록 설정
public abstract class Timestamped {

    @CreatedDate // 생성일자임을 나타냅니다.
    private LocalDateTime createdAt;
    //년 월 일만 저장하고 싶으면 LocalDate을 쓰면된다. LocalDateTime은 년월일시간
    @LastModifiedDate // 마지막 수정일자임을 나타냅니다.
    private LocalDateTime modifiedAt;
}