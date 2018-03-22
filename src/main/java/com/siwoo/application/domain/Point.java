package com.siwoo.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table
@Getter @Setter @ToString(exclude = "member")
public class Point {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private double points = 0;

    private LocalDate lastUpdate = LocalDate.now();

    @OneToOne(mappedBy = "point")
    private Member member;

    public void setMember(Member member){
        Assert.notNull(member,"Member must not be null");
        this.member = member;
        if(member.getPoint() != this){
            member.setPoint(this);
        }
    }
}
