package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Query("select pr from ProductReview pr where pr.order.user.id =:userId")
    List<ProductReview> findByUserId(@Param("userId") long userId);
    List<ProductReview> findByProductId(long productId);

    Optional<ProductReview> findByOrderIdAndProductId(long orderId, long productId);


}
