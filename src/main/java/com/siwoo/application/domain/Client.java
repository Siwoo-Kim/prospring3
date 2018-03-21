package com.siwoo.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter @ToString
public class Client {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    //
    @Column(name="name")
    private String username;
    private int age;
    @Transient
    private boolean managed = false;

    enum Type{
        BASIC(1), SILVER(5), PRIEMER(10);
        private int plusPoint;
            Type(int plusPoint) {
                this.plusPoint = plusPoint;
            }
            int getPoint(){
                return plusPoint;
            }
    }

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDate joinDate;

    @Lob
    private String description;


}
