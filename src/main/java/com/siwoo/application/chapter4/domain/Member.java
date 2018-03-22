package com.siwoo.application.chapter4.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


//@SequenceGenerator(name="member_seq_generator",
//        sequenceName = "seq_member",
//        allocationSize = 1,initialValue = 1)
@Entity @Table(name="chap4_member") @ToString(exclude = {"orders"})
@EqualsAndHashCode(of={"name","memberId"}) @Setter @Getter
public class Member implements Serializable{


    //@GeneratedValue(strategy = SEQUENCE,generator = "member_seq_generator")
    @Id @GeneratedValue
    private Long memberId;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    //testing
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("testingunit1");
        EntityManager entityManager = factory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Member member = new Member();
            member.setAddress(new Address());
            entityManager.persist(member);
            System.out.println(member.getMemberId());
            Order order = new Order();
            entityManager.persist(order);
            OrderItem orderItem = new OrderItem();
            entityManager.persist(orderItem);
            Item item = new Item();
            entityManager.persist(item);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        factory.close();
    }

}
