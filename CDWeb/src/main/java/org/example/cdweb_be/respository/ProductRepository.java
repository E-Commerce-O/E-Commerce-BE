package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.OrderItem;
import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductImage;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT pr FROM Product pr WHERE pr.name like %:productName%")
    List<Product> findByName(@Param("productName") String productName);

}
