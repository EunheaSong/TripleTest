package com.example.tripletest.entity;

public enum StatusEnum {


    ADD(+1, "적립"), MINUS(-1, "차감");

    private final int point;
    private final String status;

    StatusEnum(int point, String status) {
        this.point = point;
        this.status = status;
    }

    public int point (){
        return point;
    }
    public String status () {
        return status;
    }

}
