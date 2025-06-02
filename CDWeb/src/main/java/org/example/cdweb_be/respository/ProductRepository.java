package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    @Query("SELECT pr FROM Product pr WHERE pr.name like %:productName%")
    Page<Product> findByName(@Param("productName") String productName, Pageable pageable);
    Page<Product> findByCategoryId(long categoryId, Pageable pageable);
    @Query("select pr from Product pr where pr.id = :productId ")
    Optional<Product> findByIdAndColor(@Param("productId") long productId, @Param("colorName") String colorName);
}
