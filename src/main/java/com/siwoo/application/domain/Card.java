package com.siwoo.application.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The Card Entity.
 */
@Entity @Table(name="card")
@EqualsAndHashCode(of={"id","issueDate","point"})
@Getter @Setter @ToString
public class Card implements Serializable{

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name="issue_date")
    private LocalDate issueDate = LocalDate.now();
    private Double point = .0;
    @Version
    private int version;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Employee owner;

    public Card() {}
    public Card(LocalDate issueDate, double point) {
        this.issueDate = issueDate;
        this.point = point;
    }
}
