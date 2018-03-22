package com.siwoo.application.chapter4.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity @Table(name="chap4_item") @ToString(exclude = {"orderItems"})
@EqualsAndHashCode(of={"itemId","name","price"})
@Setter @Getter
public class Item {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name="item_id")
    private Long itemId;

    private String name;

    private double price;

    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    Set<OrderItem> orderItems = new HashSet<>();

}
