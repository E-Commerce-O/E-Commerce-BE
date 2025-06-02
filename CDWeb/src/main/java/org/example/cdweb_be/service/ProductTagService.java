package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.entity.ProductTag;
import org.example.cdweb_be.entity.Tag;
import org.example.cdweb_be.respository.ProductTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // tự khởi tạo đối tượng cho biến có final -> thay để cho autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // thêm final cho các biến
@Service
public class ProductTagService {
    MessageProvider messageProvider;
    ProductTagRepository productTagRepository;
    public List<Tag> getByProductId(long productId, int page, int size){
        Page<ProductTag> productTags = productTagRepository.findByProductId(productId, PageRequest.of(page-1, size));
        List<Tag> tags = productTags.stream().map(
                productTag -> productTag.getTag()
        ).collect(Collectors.toList());
        return tags;
    }
    public Page<ProductTag> getAll(int page, int size){
        return productTagRepository.findAll(PageRequest.of(page-1, size));
    }
}
