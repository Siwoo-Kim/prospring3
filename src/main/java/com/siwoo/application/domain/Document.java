package com.siwoo.application.domain;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table
public class Document {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="writer_id")
    private Member writer;

    private String title;

    @Column(name="create_date")
    private String createDate;

    @Lob
    private String content;
}
