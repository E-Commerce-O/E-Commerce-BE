package org.example.cdweb_be.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {
    int statusCode;
    String statusName;
    public OrderStatusResponse(OrderStatus orderStatus){
        this.statusCode = orderStatus.getStatusCode();
        this.statusName = orderStatus.getStatusName();
    }
}
