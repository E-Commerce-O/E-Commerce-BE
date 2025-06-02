package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductHistory;
import org.example.cdweb_be.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long>, PagingAndSortingRepository<ProductHistory, Long> {
    @Query("select ph.product from ProductHistory ph where ph.ip = :ip order by ph.viewAt desc")
    Page<Product> findByIp(String ip, Pageable pageable);
    int countByIp(String ip);
    Optional<ProductHistory> findByIpAndProductId(String ip, long productId);
}
