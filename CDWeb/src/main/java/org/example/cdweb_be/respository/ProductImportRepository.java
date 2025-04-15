package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.ProductImport;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImportRepository extends JpaRepository<ProductImport, Long> {
    List<ProductImport> findByProductId(long productId);
    List<ProductImport> findByUserId(long userId);
    @Query("select pi from ProductImport pi where pi.product.id = :productId " +
            "and pi.size.id = :sizeId and pi.color.id = :colorId")
    List<ProductImport> findByProductAndColorAndSize(@Param("productId") long productId
            , @Param("colorId") long colorId, @Param("sizeId") long sizeId);
    @Query("select pi from ProductImport pi where pi.product.id = :productId " +
            "and pi.size.id = :sizeId ")
    List<ProductImport> findByProductAndSize(@Param("productId") long productId
            , @Param("sizeId") long sizeId);
    @Query("select pi from ProductImport pi where pi.product.id = :productId " +
            "and pi.color.id = :colorId")
    List<ProductImport> findByProductAndColor(@Param("productId") long productId
            , @Param("colorId") long colorId);

}
