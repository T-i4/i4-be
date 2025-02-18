package com.business.i4_be.domain.store.service;

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
public class StoreService {
    private final StoreRepository storeRepository;

    /**
     * 가게 등록
     */
    @Transactional
    public StoreResDto createStore(StoreReqDto reqDto) {
        if (storeRepository.existsByStoreName(reqDto.getStoreName())) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }

        Store store = reqDto.toEntity();
        Store savedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(savedStore);
    }

    /**
     * 개별 가게 조회
     */
    public StoreResDto getStoreById(UUID storeId) {
        Store store = storeRepository.findByStoreIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return StoreResDto.fromEntity(store);
    }

    /**
     * 가게 목록 조회
     */
    public StoreListResDto getAllStores() {
        List<Store> stores = storeRepository.findAllByIsDeletedFalse();
        List<StoreResDto> storeDtos = StoreResDto.fromEntityList(stores);
        return StoreListResDto.fromEntityList(storeDtos);
    }

    /**
     * 가게 수정 (가게 이름은 수정 불가)
     */
    @Transactional
    public StoreResDto updateStore(UUID storeId, StoreUpdateReqDto updateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        updateDto.applyTo(store);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
    }

    /**
     * 가게 삭제
     */
    @Transactional
    public void deleteStore(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        store.softDelete();
        storeRepository.save(store); // 변경 사항 저장
    }
    /**
     * 삭제된 가게 조회
     * */
    public List<Store> getDeletedStores() {
        return storeRepository.findAllByIsDeletedTrue();
    }

    /**
     * 가게 이름 검색
     */
    public List<StoreResDto> searchStoresByName(String keyword) {
        List<Store> stores = storeRepository.findByStoreNameContainingAndIsDeletedFalse(keyword);
        return StoreResDto.fromEntityList(stores);
    }

    /**
     * 카테고리별 가게 조회
     */
    public List<StoreResDto> getStoresByCategory(String categoryName) {
        if (!StoreCategory.isValidCategory(categoryName)) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }
        StoreCategory category = StoreCategory.valueOf(categoryName.toUpperCase());
        List<Store> stores = storeRepository.findByCategoryAndIsDeletedFalse(category);

        return StoreResDto.fromEntityList(stores);
    }

    /**
     * 가게 상태 변경
     * statusStr 예시: "OPEN" 또는 "CLOSE"
     */
    @Transactional
    public StoreResDto updateStoreStatus(UUID storeId, String statusStr) {
        StoreIsOpen newStatus;
        try {
            newStatus = StoreIsOpen.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_STATUS);
        }
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        // 도메인 메서드 사용: updateStatus
        store.updateStatus(newStatus);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
    }

    /**
     * 가게 카테고리 변경
     * categoryStr 예시: "치킨" 또는 "피자"
     */
    @Transactional
    public StoreResDto updateStoreCategory(UUID storeId, String categoryStr) {
        StoreCategory newCategory;
        try {
            newCategory = StoreCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_CATEGORY);
        }
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        // 도메인 메서드 사용: updateCategory
        store.updateCategory(newCategory);
        Store updatedStore = storeRepository.save(store);
        return StoreResDto.fromEntity(updatedStore);
    }
}
