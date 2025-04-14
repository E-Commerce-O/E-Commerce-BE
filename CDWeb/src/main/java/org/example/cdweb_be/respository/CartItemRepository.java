package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.CartItem;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(long cartId);
    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.product.id = :productId ")
    Optional<CartItem> findByCartAndProduct(@Param("cartId") long cartId, @Param("productId") long productId);

    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.product.id = :productId and ci.color.id = :colorId")
    Optional<CartItem> findByCartAndProductAndColor(@Param("cartId") long cartId, @Param("productId") long productId, @Param("color") long colorId);
    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.product.id = :productId and ci.size.id = :sizeId")
    Optional<CartItem> findByCartAndProductAndSize(@Param("cartId") long cartId, @Param("productId") long productId, @Param("sizeId") long sizeId);
    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.product.id = :productId and ci.color.id = :colorId and ci.size.id = :sizeId")
    Optional<CartItem> findByCartAndProductAndColorAndSize(@Param("cartId") long cartId,
                                                           @Param("productId") long productId, @Param("colorId") long colorId, @Param("sizeId") long sizeId);



}
