package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Product;
import org.example.cdweb_be.entity.ProductTag;
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
public interface ProductTagRepository extends JpaRepository<ProductTag, Long>, PagingAndSortingRepository<ProductTag, Long> {
    Page<ProductTag> findAll(Pageable pageable);
    Page<ProductTag> findByProductId(long productId, Pageable pageable);
    List<ProductTag> findByProductId(long productId);
    Optional<ProductTag> findByProductIdAndTagName(long productId, String tagName);
    @Query("select pt.product from ProductTag pt where pt.tag.name = :tagName")
    Page<Product> findProductByTag(@Param("tagName") String tagName, Pageable pageable);
}
