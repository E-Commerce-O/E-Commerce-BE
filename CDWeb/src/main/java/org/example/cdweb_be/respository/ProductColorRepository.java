package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductColorRepository extends JpaRepository<Color, Long> {
}
