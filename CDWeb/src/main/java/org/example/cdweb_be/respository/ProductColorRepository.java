package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {
    List<ProductColor> findByProductId(long productId);
    Optional<ProductColor> findByIdAndProductId(long id, long productId);
    @Query("select pc from ProductColor pc where pc.product.id = :productId and pc.colorName = :colorName")
    Optional<ProductColor> findByProductAndName(@Param("productId") long productId, @Param("colorName") String colorName);
}
