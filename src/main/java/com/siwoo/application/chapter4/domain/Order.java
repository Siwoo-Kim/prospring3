package com.siwoo.application.chapter4.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

//@SequenceGenerator(name="order_seq_generator",
//        sequenceName = "seq_order",
//        initialValue = 1,allocationSize = 1)
@Entity @Table(name="chap4_member") @ToString(exclude = {"orderItems","member"})
@EqualsAndHashCode(of={"orderId","orderDate","status"}) @Setter @Getter
public class Order implements Serializable{

    @Id @GeneratedValue(strategy = AUTO)
    /*@GeneratedValue(strategy = SEQUENCE, generator = "order_seq_generator")*/
    @Column(name="order_id")
    private Long orderId;

    @ManyToOne(optional = true)
    @JoinColumn(name="member_id") //FK
    Member member;

    private LocalDate orderDate;

    public enum Status {
        CANCEL, ORDER
    }
    @Enumerated(EnumType.STRING)
    private Status status = Status.ORDER;

    @OneToMany/*(mappedBy = "order",
    cascade = CascadeType.ALL, orphanRemoval = true)*/
    Set<OrderItem> orderItems = new HashSet<>();
}
