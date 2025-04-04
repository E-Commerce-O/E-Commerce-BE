package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTagRepositoty extends JpaRepository<ProductTag, Long> {
    Optional<List<ProductTag>> findByProductId(long productId);
}
