package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    @Query("select ph.product from ProductHistory ph where ph.ip = :ip order by ph.viewAt desc")
    List<Product> findByIp(String ip);

    Optional<ProductHistory> findByIpAndProductId(String ip, long productId);
}
