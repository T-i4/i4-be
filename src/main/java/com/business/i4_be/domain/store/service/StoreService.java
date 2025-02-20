package com.business.i4_be.domain.store.service;

import com.business.i4_be.domain.order.repository.OrderRepository;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.dto.StoreReqDto;
import com.business.i4_be.domain.store.dto.StoreUpdateReqDto;
import com.business.i4_be.domain.store.dto.StoreListResDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
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

    //가게 등록
    @Transactional
    public StoreResDto createStore(StoreReqDto reqDto) {
        if (storeRepository.existsByStoreName(reqDto.getStoreName())) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }

        Store store = reqDto.toEntity();
        Store savedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(savedStore);
    }

    //가게 조회(단건)
    public StoreResDto getStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return StoreResDto.fromEntity(store);
    }

    //가게 조회(목록)
    public StoreListResDto getAllStores() {
        List<Store> stores = storeRepository.findAll();
        List<StoreResDto> storeDtos = StoreResDto.fromEntityList(stores);
        return StoreListResDto.fromEntityList(storeDtos);
    }

    //가게 수정
    @Transactional
    public StoreResDto updateStore(UUID storeId, StoreUpdateReqDto updateReqDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        if(store.isDeleted()){
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }
        updateReqDto.applyTo(store);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
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
        storeRepository.save(store);
    }

    //가게 검색(이름)
    public List<StoreResDto> searchStoresByName(String keyword) {
        List<Store> stores = storeRepository.findByStoreNameContaining(keyword);
        return StoreResDto.fromEntityList(stores);
    }

    //가게 검색(카테고리)
    public List<StoreResDto> getStoresByCategory(String categoryName) {
        if (!StoreCategory.isValidCategory(categoryName)) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }
        StoreCategory category = StoreCategory.valueOf(categoryName.toUpperCase());
        List<Store> stores = storeRepository.findByCategory(category);

        return StoreResDto.fromEntityList(stores);
    }

    //가게 수정(상태만)
    @Transactional
    public StoreResDto updateStoreStatus(UUID storeId, String isOpen) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }
        StoreIsOpen newStatus;
        try {
            newStatus = StoreIsOpen.valueOf(isOpen.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_STATUS);
        }
        store.updateStatus(newStatus);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
    }

    //가게 수정(카테고리만)
    @Transactional
    public StoreResDto updateStoreCategory(UUID storeId, String category) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }
        StoreCategory newCategory;
        try {
            newCategory = StoreCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_CATEGORY);
        }
        store.updateCategory(newCategory);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
    }
}
