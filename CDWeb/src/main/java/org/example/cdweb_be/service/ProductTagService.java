package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.entity.ProductTag;
import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.respository.ProductTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // tự khởi tạo đối tượng cho biến có final -> thay để cho autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // thêm final cho các biến
@Service
public class ProductTagService {
    ProductTagRepository productTagRepository;
    public List<Tag> getByProductId(long productId){
        List<ProductTag> productTags = productTagRepository.findByProductId(productId);
        List<Tag> tags = productTags.stream().map(
                productTag -> productTag.getTag()
        ).collect(Collectors.toList());
        return tags;
    }
    public List<ProductTag> getAll(){
        return productTagRepository.findAll();
    }
}
