package com.siwoo.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter @ToString
public class Client {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name="name")
    private String username;
    private int age;
}
