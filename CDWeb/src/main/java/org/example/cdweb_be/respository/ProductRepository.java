package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT pr FROM Product pr WHERE pr.name like %:productName%")
    List<Product> findByName(@Param("productName") String productName);
    List<Product> findByCategoryId(long categoryId);
    @Query("select pr from Product pr where pr.id = :productId ")
    Optional<Product> findByIdAndColor(@Param("productId") long productId, @Param("colorName") String colorName);
}
