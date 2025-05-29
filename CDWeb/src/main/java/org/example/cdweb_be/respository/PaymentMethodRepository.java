package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    @Query("select pm from PaymentMethod pm where pm.isActive = true")
    List<PaymentMethod> findAllActive();
}
