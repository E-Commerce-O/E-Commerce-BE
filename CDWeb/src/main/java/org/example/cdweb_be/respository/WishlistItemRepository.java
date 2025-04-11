package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserId(long userId);
    Optional<WishlistItem> findByUserIdAndProductId(long userId, long productId);
}
