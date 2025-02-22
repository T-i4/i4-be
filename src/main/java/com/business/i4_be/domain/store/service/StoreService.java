package com.business.i4_be.domain.store.service;

import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.dto.StoreCategoryUpdateReqDto;
import com.business.i4_be.domain.store.dto.StoreReqDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
import com.business.i4_be.domain.store.dto.StoreStatusUpdateReqDto;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;
    //가게 등록
    @Transactional
    public StoreResDto createStore(StoreReqDto reqDto) {
        if (storeRepository.existsByStoreName(reqDto.getStoreDto().getStoreName())) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }

        Store store = reqDto.toEntity();
        storeRepository.save(store);
        return StoreResDto.fromEntity(store);
    }

    //가게 조회(단건)
    public StoreResDto getStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return StoreResDto.fromEntity(store);
    }

    //가게 조회(목록)
    public StoreResDto.StoreListResDto getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return StoreResDto.StoreListResDto.fromEntityList(stores);
    }

    //가게 수정
    @Transactional
    public StoreResDto updateStore(UUID storeId, StoreReqDto updateReqDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        updateReqDto.applyTo(store);
        return StoreResDto.fromEntity(store);
    }

    //가게 삭제
    @Transactional
    public void deleteStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.delete("test_user");
    }

    //가게 검색(이름)
    public List<StoreResDto> searchStoresByName(String keyword) {
        List<Store> stores = storeRepository.findByStoreNameContaining(keyword);
        return stores.stream()
                .map(StoreResDto::fromEntity)
                .toList();
    }

    //가게 검색(카테고리)
    public List<StoreResDto> getStoresByCategory(String categoryName) {
        if (!StoreCategory.isValidCategory(categoryName)) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        StoreCategory category = StoreCategory.valueOf(categoryName.toUpperCase());
        List<Store> stores = storeRepository.findByCategory(category);
        return stores.stream()
                .map(StoreResDto::fromEntity)
                .toList();
    }

    /**
     * 가게 상태 변경 (OPEN / CLOSED)
     */
    @Transactional
    public StoreResDto updateStoreStatus(UUID storeId, StoreStatusUpdateReqDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.updateStatus(requestDto.getIsOpen()); // 상태 업데이트

        return StoreResDto.fromEntity(store); // 변경된 Store 반환
    }

    /**
     * 가게 카테고리 변경
     */
    @Transactional
    public StoreResDto updateStoreCategory(UUID storeId, StoreCategoryUpdateReqDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.updateCategory(requestDto.getCategory()); // 카테고리 업데이트

        return StoreResDto.fromEntity(store); // 변경된 Store 반환
    }

    //특정 가게의 주문 목록 조회 (OWNER)
    public List<OrderResDto> getOrdersByStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        List<Order> orders = orderRepository.findByStore(store);
        return orders.stream()
                .map(OrderResDto::from)
                .toList();
    }
}
