package com.example.entity;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = -8210461306842344192L;

    private Integer id;
    private String name;

    public Order() {
    }

    public Order(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
