package org.example.cdweb_be.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONArray;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum OrderStatus {
    CHO_THANH_TOAN(OrderStatus.ST_CHO_THANH_TOAN, "delay payment"),
    DAT_HANG_TC(OrderStatus.ST_DAT_HANG_TC, "Order successful"),
    DANG_CBI_HANG(OrderStatus.ST_DANG_CBI_HANG, "Preparing order"),
    DVVC_LAY_HANG(OrderStatus.ST_DVVC_LAY_HANG, "The shipping unit has received the order successfully"),
    DANG_VAN_CHUYEN(OrderStatus.ST_DANG_VAN_CHUYEN, "Order is being shipped"),
    DANG_GIAO(OrderStatus.ST_DANG_GIAO, "Being delivered to you"),
    GIAO_THANH_CONG(OrderStatus.ST_GIAO_THANH_CONG, "Delivery successful"),
    DA_HUY(OrderStatus.ST_DA_HUY, "Canceled"),
    TRA_HANG(OrderStatus.ST_YC_TRA_HANG, "Return Request"),
    DA_TRA_HANG(OrderStatus.ST_DA_TRA_HANG, "Returned")


    ;
    public static final int
            ST_CHO_THANH_TOAN = -1,
            ST_DAT_HANG_TC = 0,
            ST_DANG_CBI_HANG = 1,
            ST_DVVC_LAY_HANG = 2,
            ST_DANG_VAN_CHUYEN = 3,
            ST_DANG_GIAO = 4,
            ST_GIAO_THANH_CONG = 5,
            ST_DA_HUY = 6,
            ST_YC_TRA_HANG =7,
            ST_DA_TRA_HANG = 8;
    private int statusCode;
    private String statusName;
    public static OrderStatus getByStatusCode(int statusCode){
        OrderStatus orderStatus = null;
        for(OrderStatus os: OrderStatus.values()){
            if(os.statusCode == statusCode){
                orderStatus = os;
                break;
            }
        }
        return orderStatus;
    }
    public static String getAllStatus(){
       String reuslt="[";
        for(int i=0; i<OrderStatus.values().length; i++){
            OrderStatus os = OrderStatus.values()[i];
            reuslt += os.statusCode+":"+os.statusName;
            if(i < OrderStatus.values().length -1) reuslt+="; ";
        }
        return reuslt+="]";
    }
}
