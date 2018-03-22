package com.siwoo.application.domain;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter @ToString
public class MemberSummary implements Serializable{
    private String username;
    private LocalDate joinDate;
    private double point;

    protected MemberSummary() { }

    public MemberSummary(String username, LocalDate joinDate, double point) {
        this.username = username;
        this.joinDate = joinDate;
        this.point = point;
    }

}
