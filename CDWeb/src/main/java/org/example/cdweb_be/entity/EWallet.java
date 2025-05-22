package org.example.cdweb_be.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class EWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToOne
    User user;
    double balance;
    String currency;
    boolean isActive;
    Timestamp createdAt;
    Timestamp updatedAt;

}
