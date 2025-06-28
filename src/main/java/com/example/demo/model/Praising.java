package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Praising {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    private User user;

    // Getters and Setters
    // ...
}
