package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Order;
import org.example.cdweb_be.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId AND oi.order.status <> :orderStatus")
    List<OrderItem> findByProductIdAndExceptStatus(@Param("productId") long productId, @Param("orderStatus") int orderStatus);
    @Query("select oi from OrderItem oi where oi.product.id = :productId and oi.order.status <> :orderStatus " +
            "and oi.color.id = :colorId and oi.size.id = :sizeId")
    List<OrderItem> findByProductAndColorAndSizeAndExceptStatus(@Param("productId") long productId, @Param("colorId") long colorId,
                                                                @Param("sizeId") long sizeId, @Param("orderStatus") int orderStatus);
    @Query("select oi from OrderItem oi where oi.product.id = :productId and oi.order.status <> :orderStatus " +
            "and oi.color.id = :colorId ")
    List<OrderItem> findByProductAndColorAndExceptStatus(@Param("productId") long productId, @Param("colorId") long colorId,
                                                         @Param("orderStatus") int orderStatus);
    @Query("select oi from OrderItem oi where oi.product.id = :productId and oi.order.status <> :orderStatus " +
            "and oi.size.id = :sizeId")
    List<OrderItem> findByProductAndSizeAndExceptStatus(@Param("productId") long productId,
                                                        @Param("sizeId") long sizeId, @Param("orderStatus") int orderStatus);
    List<OrderItem> findByOrderId(long orderId);
    @Query("select distinct oi.order from OrderItem oi where oi.product.id =:productId")
    List<Order> findOrderByProduct(@Param("productId") long productId);
    Optional<OrderItem> findByOrderIdAndProductId(long orderId, long productId);
}
