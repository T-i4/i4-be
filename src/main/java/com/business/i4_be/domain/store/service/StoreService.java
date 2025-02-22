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
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.entity.UserRole;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    //가게 등록
    @Transactional
    public StoreResDto createStore(StoreReqDto reqDto,Long userId) {

        // 🔍 userId로 사용자 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 🔥 [추가] MASTER 권한이 아니면 예외 발생
        if (!user.getRole().equals(UserRole.MASTER)){
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (storeRepository.existsByStoreName(reqDto.getStoreDto().getStoreName())) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }
        Store store = reqDto.toEntity(user);
        storeRepository.save(store);
        return StoreResDto.fromEntity(store);
    }

    //가게 등록 후 가게 소유자 변경
    @Transactional
    public StoreResDto updateStoreOwner(UUID storeId, Long userId, Long newUserId) {
        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 새로운 USER 조회
        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Userid(가게 소유) 변경 (MASTER 권한 체크 추가)
        if (!userRepository.existsByIdAndRole(userId, UserRole.MASTER)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        store.setUser(newUser);
        log.info("🔄 [updateStoreOwner] Store ID: {}, New Owner ID: {}", storeId, newUserId);

        return StoreResDto.fromEntity(store);
    }

    //가게 조회(단건 : ALL)
    public StoreResDto getStore(UUID storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return StoreResDto.fromEntity(store);
    }
    //가게 조회(단건 : OWNER)
    public StoreResDto getStoreForOwner(UUID storeId, Long userId) {
        Store store = storeRepository.findByStoreIdAndUserId(storeId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND_OR_NO_PERMISSION));
        return StoreResDto.fromEntity(store);
    }

    //가게 조회(목록 : ALL)
    public StoreResDto.StoreListResDto getAllStores( Long userId) {
        List<Store> stores = storeRepository.findAll();
        return StoreResDto.StoreListResDto.fromEntityList(stores);
    }

    //가게 조회(목록 : OWNER)
    public StoreResDto.StoreListResDto  getAllStoresForOwner(Long userId) {
        List<Store> stores = storeRepository.findAllByAndUserId(userId);
        return StoreResDto.StoreListResDto.fromEntityList(stores);
    }



    //가게 수정
    @Transactional
    public StoreResDto updateStore(UUID storeId, StoreReqDto updateReqDto, Long userId) {
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
    public void deleteStore(UUID storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.delete("test_user");
    }

    //가게 검색(이름)
    public List<StoreResDto> searchStoresByName(String keyword, Long userId) {
        List<Store> stores = storeRepository.findByStoreNameContaining(keyword);
        return stores.stream()
                .map(StoreResDto::fromEntity)
                .toList();
    }

    //가게 검색(카테고리)
    public List<StoreResDto> getStoresByCategory(String categoryName, Long userId) {
        if (!StoreCategory.isValidCategory(categoryName)) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        StoreCategory category = StoreCategory.valueOf(categoryName.toUpperCase());
        List<Store> stores = storeRepository.findByCategory(category);
        return stores.stream()
                .map(StoreResDto::fromEntity)
                .toList();
    }

    //가게 상태 변경 (OPEN / CLOSED)
    @Transactional
    public StoreResDto updateStoreStatus(UUID storeId, StoreStatusUpdateReqDto requestDto, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.updateStatus(requestDto.getIsOpen()); // 상태 업데이트

        return StoreResDto.fromEntity(store); // 변경된 Store 반환
    }

    //가게 카테고리 변경
    @Transactional
    public StoreResDto updateStoreCategory(UUID storeId, StoreCategoryUpdateReqDto requestDto, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (store.isDeleted()) {
            throw new CustomException(ErrorCode.STORE_ALREADY_DELETED);
        }

        store.updateCategory(requestDto.getCategory()); // 카테고리 업데이트

        return StoreResDto.fromEntity(store); // 변경된 Store 반환
    }

    //특정 가게의 주문 목록 조회 (OWNER)
    public List<OrderResDto> getOrdersByStore(UUID storeId, Long userId) {
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
