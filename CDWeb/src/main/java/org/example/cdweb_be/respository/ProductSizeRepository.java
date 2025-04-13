package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeRepository extends JpaRepository<Size, Long> {
}
