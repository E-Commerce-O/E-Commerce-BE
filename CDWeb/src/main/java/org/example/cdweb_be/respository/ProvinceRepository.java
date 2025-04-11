package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Query("SELECT p FROM Province p WHERE p.name LIKE :provinceName%")
    List<Province> findByName(@Param("provinceName") String provinceName);

}
