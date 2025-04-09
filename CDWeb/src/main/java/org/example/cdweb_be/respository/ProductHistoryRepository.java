package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, String> {
}
