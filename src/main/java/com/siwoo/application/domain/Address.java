package com.siwoo.application.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@ToString
@Embeddable
@EqualsAndHashCode(of={"zip","street","city"})
public class Address implements Serializable{

    private String zip;
    private String street;
    private String city;

    //Necessary
    protected Address() { }

    public Address(String zip, String street, String city) {
        this.zip = zip;
        this.street = street;
        this.city = city;
    }
}
