package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByEmail(String email);
    Optional<OTP> findByUsername(String username);
    Optional<OTP> findByVerified(String verified);
    @Modifying
    @Query("delete from OTP where email=:email")
    void deleteByEmail(@Param("email") String email);
}
