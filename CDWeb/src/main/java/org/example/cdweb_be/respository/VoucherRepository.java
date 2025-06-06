package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.Voucher;
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
public interface VoucherRepository extends JpaRepository<Voucher, Long>, PagingAndSortingRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);
    @Query("select vc from Voucher  vc where vc.endAt > now()")
    Page<Voucher> findAllValid(Pageable pageable);
    @Query("select vc from Voucher  vc where vc.code = :code and vc.endAt > now()")
    Optional<Voucher> findByCodeValid(@Param("code") String code);
    @Query("select vc from Voucher  vc where vc.type = :type and vc.endAt > now()")

    Page<Voucher> findByType(@Param("type") int type, Pageable pageable);
    int countByType(int type);
}
