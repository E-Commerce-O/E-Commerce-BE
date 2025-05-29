package org.example.cdweb_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {
    @Id
    long id;
    String methodName;
    String description;
    boolean isActive;
    double transactionFee;
}
