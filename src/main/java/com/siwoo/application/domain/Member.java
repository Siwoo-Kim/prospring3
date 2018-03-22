package com.siwoo.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table
@Getter @Setter @ToString(exclude = {"point","documents"})
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name="name")
    private String username;

    private Integer age;

    public enum Type{
        BASIC, SILVER, PREIMER;
    }
    @Enumerated(EnumType.STRING)
    private Type type = Type.BASIC;

    @Column(name="join_date")
    private LocalDate joinDate = LocalDate.now();

    @Column(name="update_date")
    private LocalDate updateDate;

    @Lob
    private String description;

    /*
    * @Embedded
    * Specifies a persistent field or property of an entity whose
    * value is an instance of an embeddable class. The embeddable
    * class must be annotated as {@link Embeddable}.
    */
    @Embedded
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="point_id")
    private Point point;

    @OneToMany(mappedBy = "writer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();
}
