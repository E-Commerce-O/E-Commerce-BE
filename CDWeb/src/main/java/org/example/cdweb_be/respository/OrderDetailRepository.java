package org.example.cdweb_be.respository;

import org.example.cdweb_be.entity.OrderDetail;
import org.example.cdweb_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select od from OrderDetail od where od.order.status <> :orderStatus and (od.productVoucher.id = :voucherId or od.shipVoucher.id =:voucherId)")
    List<OrderDetail> getByVoucherExceptStatus(@Param("voucherId") long voucherId, @Param("orderStatus") int orderStatus);

}
