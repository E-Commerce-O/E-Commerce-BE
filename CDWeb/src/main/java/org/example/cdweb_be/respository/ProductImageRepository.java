package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(long productId);
    @Query("select pi.imagePath from ProductImage pi where pi.product.id =:productId")
    List<String> findImagePathByProduct(@Param("productId") long productId);
}
