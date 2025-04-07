package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
}
