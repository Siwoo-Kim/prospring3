package com.siwoo.application.domain;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString @Getter
public class EmployeeSummary implements Serializable{

    private String firstName;
    private String lastName;
    private double point;

    public EmployeeSummary(String firstName, String lastName, double point) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.point = point;
    }

}
