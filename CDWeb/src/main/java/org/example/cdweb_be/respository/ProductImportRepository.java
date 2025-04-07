package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductImport;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImportRepository extends JpaRepository<ProductImport, Long> {
    List<ProductImport> findByProductId(long productId);
}
