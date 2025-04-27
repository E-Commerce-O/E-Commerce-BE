package org.example.cdweb_be.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter

public enum VoucherType {
    FREESHIP(0, "FREESHIP"),
    PRODUCT(1, "PRODUCT")
    ;
//    public static final int FREESHIP = 0, SHOPVC =1;
    int type;
    String typeName;
    VoucherType(int type, String typeName){
        this.type = type;
        this.typeName = typeName;
    }
    public static String getError(){
        String result ="Voucher tyle must be:";
        VoucherType[] vts = VoucherType.values();
        for(int i=0; i<vts.length; i++){
            if(i >0) result +=" or";
            result += " "+vts[i].type+" ("+vts[i].typeName+")";
        }
        return result;
    }

    public static boolean contain(int type) {
        return Arrays.stream(VoucherType.values())
                .anyMatch(voucherType -> voucherType.getType() == type);
    }
}
