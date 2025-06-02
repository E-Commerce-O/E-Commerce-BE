package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends PagingAndSortingRepository<ProductReview, Long>, JpaRepository<ProductReview, Long> {
    @Query("select pr from ProductReview pr where pr.order.user.id =:userId")
    List<ProductReview> findByUserId(@Param("userId") long userId);
    @Query("select pr from ProductReview pr where pr.product.id = :productId")
    Page<ProductReview> findByProductId(@Param("productId") long productId, Pageable pageable);
    @Query("select pr from ProductReview pr where pr.product.id = :productId")
    List<ProductReview> findAllByProductId(@Param("productId") long productId);

    Optional<ProductReview> findByOrderIdAndProductId(long orderId, long productId);


}
