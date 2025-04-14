package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    List<ProductSize> findByProductId(long productId);
    Optional<ProductSize> findByIdAndProductId(long id, long productId);
    @Query("select ps from ProductSize ps where ps.product.id = :productId and ps.size = :sizeName")
    Optional<ProductSize> findByProductAndName(@Param("productId") long productId, @Param("sizeName") String sizeName);
}
