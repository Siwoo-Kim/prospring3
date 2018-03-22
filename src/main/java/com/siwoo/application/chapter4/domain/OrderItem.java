package com.siwoo.application.chapter4.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

/*
    Bridge Entity
*/
@Entity @Table(name="chap4_order") @ToString(exclude = {"order","item"})
@EqualsAndHashCode(of={"order","item","orderItemId"})
@Setter @Getter
public class OrderItem {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name="order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    @Column(name="order_price")
    private double orderPrice;

    private int count;

}
