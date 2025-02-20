package com.business.i4_be.domain.store.controller;

import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.store.dto.StoreCategoryUpdateReqDto;
import com.business.i4_be.domain.store.dto.StoreReqDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
import com.business.i4_be.domain.store.dto.StoreStatusUpdateReqDto;
import com.business.i4_be.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    //가게 등록
    @PostMapping("/v1/stores")
    public ResponseEntity<StoreResDto> createStore(@Valid @RequestBody StoreReqDto reqDto) {
        StoreResDto newStore = storeService.createStore(reqDto);
        return ResponseEntity.ok(newStore);

    }
    //가게 조회(단건)
    @GetMapping("/v1/stores/{storeId}")
    public ResponseEntity<StoreResDto> getStore(@PathVariable UUID storeId) {
        StoreResDto store = storeService.getStore(storeId);
        return ResponseEntity.ok(store);
    }

    //가게 목록 조회
    @GetMapping("v1/stores")
    public ResponseEntity<StoreResDto.StoreListResDto> getAllStores() {
        StoreResDto.StoreListResDto stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    //가게 수정 (이름 변경 불가)
    @PutMapping("/owner/v1/stores/{storeId}")
    public ResponseEntity<StoreResDto> updateStore(@PathVariable UUID storeId,
                                                   @Validated(StoreReqDto.Update.class) @RequestBody StoreReqDto updateReqDto) {
        StoreResDto updatedStore = storeService.updateStore(storeId, updateReqDto);
        return ResponseEntity.ok(updatedStore);
    }

    //가게 삭제
    @DeleteMapping("/v1/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }

    //가게 검색(이름)
    @GetMapping("/v1/stores/search")
    public ResponseEntity<List<StoreResDto>> searchStoresByName(@RequestParam String keyword) {
        List<StoreResDto> stores = storeService.searchStoresByName(keyword);
        return ResponseEntity.ok(stores);
    }

    /**
     * 가게 검색 (카테고리)
     */
    @GetMapping("/v1/stores/category")
    public ResponseEntity<List<StoreResDto>> getStoresByCategory(@RequestParam String category) {
        List<StoreResDto> stores = storeService.getStoresByCategory(category);
        return ResponseEntity.ok(stores);
    }

    /**
     * 가게 상태 변경 (OPEN / CLOSED)
     */
    @PatchMapping("/owner/v1/stores/{storeId}/status")
    public ResponseEntity<StoreResDto> updateStoreStatus(@PathVariable UUID storeId,
                                                         @RequestBody @Valid StoreStatusUpdateReqDto requestDto) {
        StoreResDto updatedStore = storeService.updateStoreStatus(storeId, requestDto);
        return ResponseEntity.ok(updatedStore);
    }

    /**
     * 가게 카테고리 변경
     */
    @PatchMapping("/owner/v1/stores/{storeId}/category")
    public ResponseEntity<StoreResDto> updateStoreCategory(@PathVariable UUID storeId,
                                                           @RequestBody @Valid StoreCategoryUpdateReqDto requestDto) {
        StoreResDto updatedStore = storeService.updateStoreCategory(storeId, requestDto);
        return ResponseEntity.ok(updatedStore);
    }
    /**
     * 특정 가게의 주문 목록 조회 (가게 주인용)
     */
    @GetMapping("/owner/{storeId}/orders")
    public ResponseEntity<List<OrderResDto>> getOrdersByStore(@PathVariable UUID storeId) {
        List<OrderResDto> orders = storeService.getOrdersByStore(storeId);
        return ResponseEntity.ok(orders);
    }
}












