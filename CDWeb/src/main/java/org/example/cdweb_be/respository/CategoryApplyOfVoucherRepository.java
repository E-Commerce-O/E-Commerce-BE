package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.CategoryApplyOfVouhcer;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryApplyOfVoucherRepository extends JpaRepository<CategoryApplyOfVouhcer, Long> {
    List<CategoryApplyOfVouhcer> findByVoucherId(long voucherId);
    List<CategoryApplyOfVouhcer> findByCategoryId(long categoryId);
    Optional<CategoryApplyOfVouhcer> findByCategoryIdAndVoucherId(long categoryId, long voucherId);

}
