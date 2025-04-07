package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.OrderItem;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId AND oi.order.status <> :orderStatus")
    List<OrderItem> findOrderItemsByProductIdAndOrderStatus(@Param("productId") long productId, @Param("orderStatus") int orderStatus);
}
