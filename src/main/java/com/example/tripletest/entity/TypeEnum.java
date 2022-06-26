package com.example.tripletest.entity;

public enum TypeEnum {
    FIRST("첫 리뷰"), ADDREVIEW("리뷰 등록"), ADDPHOTO("이미지 등록"),
    DELETEFIRST("첫 리뷰 삭제"),DELETEREVIEW("리뷰삭제"), DELETEPHOTO("이미지 삭제");

    private final String status;

    TypeEnum(String status) {
        this.status = status;
    }

    public String status () {
        return status;
    }

}
