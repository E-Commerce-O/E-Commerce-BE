package org.example.cdweb_be.respository;

import org.aspectj.apache.bcel.classfile.Module;
import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(long userId);
    @Query("SELECT ad FROM Address ad WHERE ad.user.id = :userId " +
            "AND ad.province.id = :districtId AND ad.ward.id = :wardId AND ad.houseNumber = :houseNumber")
    Optional<Address> findByAllInfo(@Param("userId") long userId, @Param("provinceId") long provinceId
            , @Param("districtId") long districtId, @Param("wardId") long wardId, @Param("houseNumber") String houseNumber);
}
