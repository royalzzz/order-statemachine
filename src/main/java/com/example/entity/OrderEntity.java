package com.example.entity;

public class OrderEntity {

    int id;
    String state;

    public OrderEntity(int id, String state) {
        this.id = id;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", state=" + state + "]";
    }
}
