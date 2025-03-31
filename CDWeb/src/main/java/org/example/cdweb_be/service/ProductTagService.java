package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.ProductTag;
import org.example.cdweb_be.respository.ProductTagRepositoty;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // tự khởi tạo đối tượng cho biến có final -> thay để cho autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // thêm final cho các biến
@Service
public class ProductTagService {
    ProductTagRepositoty productTagRepositoty;
    public List<ProductTag> getByProductId(long productId){
        return productTagRepositoty.findByProductId(productId).get();
    }
    public List<ProductTag> getAll(){
        return productTagRepositoty.findAll();
    }
}
