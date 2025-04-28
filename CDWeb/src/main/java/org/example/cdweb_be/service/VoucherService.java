package org.example.cdweb_be.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.ApplyVoucherRequest;
import org.example.cdweb_be.dto.request.VoucherRequest;
import org.example.cdweb_be.dto.response.CategoryVoucher;
import org.example.cdweb_be.dto.response.VoucherResponse;
import org.example.cdweb_be.entity.*;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.enums.VoucherType;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.CategoryMapper;
import org.example.cdweb_be.mapper.VoucherMapper;
import org.example.cdweb_be.respository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService {
    VoucherMapper voucherMapper;
    VoucherRepository voucherRepository;
    CategoryRepository categoryRepository;
    CategoryApplyOfVoucherRepository categoryApplyOfVoucherRepository;
    CategoryMapper categoryMapper;
    CartItemRepository cartItemRepository;
    OrderDetailRepository orderDetailRepository;
    ProductService productService;

    public VoucherResponse add(VoucherRequest request) {
        validationRequest(request);
        Optional<Voucher> voucherOptional = voucherRepository.findByCode(request.getCode());
        if (voucherOptional.isPresent()) throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        Set<Category> categories = new HashSet<>();
        for (long categoryId : request.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new AppException(ErrorCode.CATEGORY_NOT_EXISTS.setMessage("CategoryId: " + categoryId + " not exists!")));
            categories.add(category);
        }
        if (categories.size() != request.getCategoryIds().size()) {
            throw new AppException(ErrorCode.CATEGORY_DUPLICATE);
        }
        Voucher voucher = voucherMapper.toVoucber(request);
        voucher.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        voucher = voucherRepository.save(voucher);
        for (Category category : categories) {
            categoryApplyOfVoucherRepository.save(CategoryApplyOfVouhcer.builder()
                    .voucher(voucher)
                    .category(category)
                    .build());
        }
        VoucherResponse voucherResponse = voucherMapper.toVoucherResponse(voucher);
        List<CategoryVoucher> categoryVouchers = categories.stream().map(categoryMapper::toCategoryVoucher).collect(Collectors.toList());
        voucherResponse.setCategoriesApply(categoryVouchers);
        return voucherResponse;
    }

    public VoucherResponse update(long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.VOUCHER_NOT_EXISTS));
        validationRequest(request);
        voucher = voucherMapper.toVoucber(request);
        voucher.setId(id);
        List<CategoryApplyOfVouhcer> categoryApplyOfVouhcers = categoryApplyOfVoucherRepository.findByVoucherId(id);
        categoryApplyOfVoucherRepository.deleteAll(categoryApplyOfVouhcers);
        voucher.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        voucher = voucherRepository.save(voucher);
        Set<Category> categories = new HashSet<>();
        for (long categoryId : request.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new AppException(ErrorCode.CATEGORY_NOT_EXISTS.setMessage("CategoryId: " + categoryId + " not exists!")));
            categories.add(category);
        }
        if (categories.size() != request.getCategoryIds().size()) {
            throw new AppException(ErrorCode.CATEGORY_DUPLICATE);
        }
        for (Category category : categories) {
            categoryApplyOfVoucherRepository.save(CategoryApplyOfVouhcer.builder()
                    .voucher(voucher)
                    .category(category)
                    .build());
        }
        return convertToVoucherResponse(voucher);
    }

    public double applyVouhcer(ApplyVoucherRequest request) {
        double result = 0;
        Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(() ->
                new AppException(ErrorCode.VOUCHER_NOT_EXISTS));
        int quantity = getRemainingQuantity(voucher);
        if (quantity <= 0) throw new AppException(ErrorCode.VOUCHER_EXPIRED_QUANTITY);
        if (voucher.getEndAt().before(new Timestamp(System.currentTimeMillis())))
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        if(request.getCartItemIds().size() ==0) throw new AppException(ErrorCode.CART_ITEM_REQUIRED);
        List<CartItem> cartItems = new ArrayList<>();
        List<Category> categoriesOfVoucher = categoryApplyOfVoucherRepository.findByVoucherId(voucher.getId())
                .stream().map(categoryApplyOfVouhcer -> categoryApplyOfVouhcer.getCategory()).collect(Collectors.toList());
        for (long id : request.getCartItemIds()) {
            CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() ->
                    new AppException(ErrorCode.CART_ITEM_NOT_EXISTS));
            cartItems.add(cartItem);
        }
        double totalPriceValid = 0;
        for (CartItem ci : cartItems) {
            double productPrice = productService.getPrice(ci.getProduct(), ci.getSize(), ci.getColor());
            if (categoriesOfVoucher.size() == 0) {
                totalPriceValid += ci.getQuantity() * productPrice;
            } else if (categoriesOfVoucher.contains(ci.getProduct().getCategory())) {
                totalPriceValid += ci.getQuantity() * productPrice;
            }
        }
        if(totalPriceValid < voucher.getMinPrice()) throw new AppException(ErrorCode.VOUCHER_CANT_APPLY);
        if(voucher.getType() == VoucherType.FREESHIP.getType()){
            if(request.getShippingCost() ==0) throw new AppException(ErrorCode.SHIPPING_COST_REQUIRED);
            result = Math.min(voucher.getMaxDecrease(), request.getShippingCost() * voucher.getPercentDecrease() / 100);
        }else{
            result = Math.min(voucher.getMaxDecrease(), totalPriceValid * voucher.getPercentDecrease() / 100);
        }
        return result;
    }

    public List<VoucherResponse> getAll() {
        List<Voucher> vouchers = voucherRepository.findAllValid();
        return vouchers.stream().map(voucher -> convertToVoucherResponse(voucher)).collect(Collectors.toList());
    }

    public List<VoucherResponse> getByType(int type) {
        if (!VoucherType.contain(type)) throw new AppException(ErrorCode.VOUCHER_TYPE_INVALID);
        List<Voucher> vouchers = voucherRepository.findByType(type);
        return vouchers.stream().map(voucher -> convertToVoucherResponse(voucher)).collect(Collectors.toList());
    }

    public VoucherResponse getByCode(String code) {
        Voucher voucher = voucherRepository.findByCodeValid(code).orElseThrow(() ->
                new AppException(ErrorCode.VOUCHER_CODE_NOT_EXISTS));
        return convertToVoucherResponse(voucher);
    }

    public VoucherResponse convertToVoucherResponse(Voucher voucher) {
        VoucherResponse voucherResponse = voucherMapper.toVoucherResponse(voucher);
        List<CategoryApplyOfVouhcer> categoryApplyOfVouhcers = categoryApplyOfVoucherRepository.findByVoucherId(voucher.getId());
        List<CategoryVoucher> categoryVouchers = categoryApplyOfVouhcers.stream().map(categoryApplyOfVouhcer ->
                categoryMapper.toCategoryVoucher(categoryApplyOfVouhcer.getCategory())).collect(Collectors.toList());
        voucherResponse.setCategoriesApply(categoryVouchers);
        voucherResponse.setQuantity(getRemainingQuantity(voucher));
        return voucherResponse;
    }

    public void validationRequest(VoucherRequest request) {
        if (!VoucherType.contain(request.getType())) throw new AppException(ErrorCode.VOUCHER_TYPE_INVALID);

        if (request.getCode().length() < 6) throw new AppException(ErrorCode.VOUCHER_CODE_INVALD);
        if (request.getPercentDecrease() < 0 || request.getPercentDecrease() > 100)
            throw new AppException(ErrorCode.VOUCHER_PERCENT_DECREASE_INVALD);
        if (request.getQuantity() < 0) throw new AppException(ErrorCode.VOUCHER_QUANTITY_INVALD);
        if (request.getMinPrice() < 0) throw new AppException(ErrorCode.VOUCHER_MIN_PRICE_INVALD);
        if (request.getEndAt().before(request.getStartAt()))
            throw new AppException(ErrorCode.VOUCHER_END_AT_INVALD);

    }

    public int getRemainingQuantity(Voucher voucher) {
        List<OrderDetail> orderDetails = orderDetailRepository.getByVoucherExceptStatus(voucher.getId(), OrderStatus.ST_DA_HUY);
        return Math.max(0, voucher.getQuantity() - orderDetails.size());
    }

    public String genCode() {
        String code = "";
        while (true) {
            code = createVoucherCode();
            Optional<Voucher> voucherOptional = voucherRepository.findByCode(code);
            if (voucherOptional.isEmpty()) break;
        }
        return code;
    }

    protected String createVoucherCode() {
        String result = "";
        Random rd = new Random();
        while (result.length() < 9) {
            if (rd.nextInt(9) % 2 == 0) {
                int c = rd.nextInt(25) + 65;
                if (rd.nextInt(10) % 2 == 0) {
                    c += 32;
                }
                result += (char) c;
            } else {
                result += rd.nextInt(10);
            }
        }
        return result;
    }
}
