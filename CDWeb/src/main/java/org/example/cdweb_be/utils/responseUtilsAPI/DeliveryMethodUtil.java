package org.example.cdweb_be.utils.responseUtilsAPI;

import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryMethodUtil {
    @SerializedName("TEN_DICHVU")
    String ten_dichvu;
    @SerializedName("THOI_GIAN")
    String thoi_gian;
    @SerializedName("GIA_CUOC")
    String gia_cuoc;
}
