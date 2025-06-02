package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Order;
import org.example.cdweb_be.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
    int countByUserId(long userId);
    int countByStatus(int status);
    Page<Order> findByUserId(long userId, Pageable pageable);
    List<Order> findByStatus(int status);
    Page<Order> findByStatus(int status, Pageable pageable);

}
