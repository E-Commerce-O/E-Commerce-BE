package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.District;
import org.example.cdweb_be.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    @Query("SELECT w FROM Ward w WHERE w.districtId = :districtId")
    List<Ward> findByDistrictId(@Param("districtId") long districtId);
    @Query("SELECT w FROM Ward w WHERE w.districtId = :districtId AND w.name LIKE :wardName%")
    List<Ward> findByDistrictIdAndName(@Param("districtId") long districtId, @Param("wardName") String wardName);
    Optional<Ward> findByIdAndDistrictId(long wardId, long districtId);
}
