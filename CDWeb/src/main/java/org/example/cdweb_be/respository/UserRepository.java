package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Page<User> findAll(Pageable pageable);
    @Query("select u from User u where u.fullName like %:name% and u.role = :role")
    Page<User> findAll(Pageable pageable, @Param("name") String name,@Param("role") String role);
    @Query("select count(u) from User u where u.fullName like %:name% and u.role = :role")
    long countAll(@Param("name") String name,@Param("role") String role);
    @Query("select u from User u where u.role = :role")

    Page<User> findAllByRole(Pageable pageable, @Param("role") String role);
    @Query("select count(u) from User u where u.role = :role")

    long countAllByRole(@Param("role") String role);
    @Query("select u from User u where u.fullName like %:name%")

    Page<User> findAllByName(Pageable pageable, @Param("name") String name);
    @Query("select count(u) from User u where u.fullName like %:name%")

    long countAllByName(@Param("name") String name);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
