package com.siwoo.application.domain;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * The Employee Entity.
 */
@Entity @Table(name="employee")
@EqualsAndHashCode(of={"id","firstName","lastName"})
@Getter @Setter @ToString(exclude = {"cards","projects"})
@NamedQueries({
        @NamedQuery(
                name = "Employee.FINDALL",
                query = "select e from Employee e"),
        @NamedQuery(
                name = "Employee.FINDALLWITHASSOCIATION",
                query = "select distinct e from Employee e " +
                        "left join fetch e.cards c " +
                        "left join fetch e.projects p "),
        @NamedQuery(
                name = "Employee.FINDBYID",
                query = "select distinct e from Employee e " +
                        "left join fetch e.cards c " +
                        "left join fetch e.projects p " +
                        "where e.id = :id ")})
public class Employee implements Serializable {

    /**
     * The constant NAMED_SQL_FINDALL.
     */
    public static final String NAMED_SQL_FINDALL = "Employee.FINDALL";
    /**
     * The constant NAMED_SQL_FINDALL_WITH_ASSOCIATION.
     */
    public static final String NAMED_SQL_FINDALL_WITH_ASSOCIATION = "Employee.FINDALLWITHASSOCIATION";
    /**
     * The constant NAMED_SQL_FIND_BY_ID.
     */
    public static final String NAMED_SQL_FIND_BY_ID = "Employee.FINDBYID";

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="hire_date")
    private LocalDate hireDate;
    @Version
    private int version;
    @OneToMany(mappedBy = "owner",
    cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();
    @ManyToMany
    @JoinTable(name="employee_project",
    joinColumns = @JoinColumn(name="employee_id"),
    inverseJoinColumns = @JoinColumn(name="project_id"))
    private Set<Project> projects = new HashSet<>();

    public Employee() {}

    public Employee(String firstName, String lastName, LocalDate hireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
    }

    public void addCard(Card card) {
        Assert.notNull(card);
        if(card.getOwner() != this){
            card.setOwner(this);
        }
        cards.add(card);
    }
}
