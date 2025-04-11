package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    @Query("SELECT dt FROM District dt WHERE dt.provinceId=:provinceId")
    List<District> findByProvinceId(@Param("provinceId") long provinceId);

    @Query("SELECT dt FROM District dt WHERE dt.provinceId=:provinceId AND dt.name LIKE :districtName%")
    List<District> findByProvinceIdAndName(@Param("provinceId") long provinceId
            , @Param("districtName") String districtName);
    Optional<District> findByIdAndProvinceId(long districtId, long provinceId);
}
