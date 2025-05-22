package org.example.cdweb_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PaymentMethod {
    @Id
    long id;
    String methodName;
    String description;
    boolean isActive;
    double transactionFee;
}
