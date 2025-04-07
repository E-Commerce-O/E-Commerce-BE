package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductDetail;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    Optional<ProductDetail> findByProductIdAndProductColorIdAndProductSizeId(long productId, long productColorId, long productSizeId);
    List<ProductDetail> findByProductId(long productId);
    List<ProductDetail> findByProductIdAndProductColorId(long productId, long colorId);
    List<ProductDetail> findByProductIdAndProductSizeId(long productId, long sizeId);
}
