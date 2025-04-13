package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductDetail;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    Optional<ProductDetail> findByProductIdAndColorIdAndSizeId(long productId, long productColorId, long productSizeId);
    List<ProductDetail> findByProductId(long productId);
    List<ProductDetail> findByProductIdAndColorId(long productId, long colorId);
    List<ProductDetail> findByProductIdAndSizeId(long productId, long sizeId);
    @Modifying
    @Query("delete from ProductDetail pd where pd.color is null and pd.product.id = :productId")
    void deleteNullColor(@Param("productId") long productId);
    @Modifying
    @Query("delete from ProductDetail pd where pd.size is null and pd.product.id = :productId")
    void deleteNullSize(@Param("productId") long productId);
}
