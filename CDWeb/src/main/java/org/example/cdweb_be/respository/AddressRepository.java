package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Address;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
