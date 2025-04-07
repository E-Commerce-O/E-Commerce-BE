package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
//    Optional<Tag> getTagByName(String name);

}
