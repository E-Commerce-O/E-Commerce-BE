package org.example.cdweb_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

@Entity
public class Transaction {
    @Id
    long id;
    @ManyToOne
    EWallet eWallet;
    double amount;
    String source;
    Timestamp createdAt;

}
