package com.siwoo.application.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The Project Entity.
 */
@Entity @Table(name="project")
@EqualsAndHashCode(of={"name"})
@Getter @Setter @ToString
public class Project implements Serializable{

    @Id @Column(name="project_name")
    private String name;
    @Column(name="embark_date")
    private LocalDate embarkDate;
    @ManyToMany
    @JoinTable(name = "employee_project",
    joinColumns = @JoinColumn(name="project_name"),
    inverseJoinColumns = @JoinColumn(name="employee_id"))
    private Set<Employee> employees = new HashSet<>();

}
