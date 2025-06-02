package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long>, PagingAndSortingRepository<WishlistItem, Long> {
    Page<WishlistItem> findByUserId(long userId, Pageable pageable);
    int countByUserId(long userId);
    Optional<WishlistItem> findByUserIdAndProductId(long userId, long productId);
}
