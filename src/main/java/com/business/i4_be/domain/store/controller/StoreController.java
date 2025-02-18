package com.business.i4_be.domain.store.controller;

import com.business.i4_be.domain.store.dto.StoreReqDto;
import com.business.i4_be.domain.store.dto.StoreUpdateReqDto;
import com.business.i4_be.domain.store.dto.StoreListResDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
    * 가게 등록 (ADMIN, MASTER)
    * */
    @PostMapping("/v1/stores")
    public ResponseEntity<StoreResDto> createStore(@Valid @RequestBody StoreReqDto reqDto) {

        StoreResDto newStore = storeService.createStore(reqDto);
        return ResponseEntity.ok(newStore);

    }
    /**
     * 개별 가게 상세 조회 (ALL)
     */
    @GetMapping("/v1/stores/{storeId}")
    public ResponseEntity<StoreResDto> getStore(@PathVariable UUID storeId) {
        StoreResDto store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(store);
    }
    /**
     * 가게 전체 조회(ALL)
     * */
    @GetMapping("/v1/stores")
    public ResponseEntity<StoreListResDto> getAllStores() {
        StoreListResDto listResponse = storeService.getAllStores();
        return ResponseEntity.ok(listResponse);
    }

    /**
     * 가게 수정 (ADMIN, MASTER, OWNER)
     */
    @PutMapping("/owner/v1/stores/{storeId}")
    public ResponseEntity<StoreResDto> updateStore(@PathVariable UUID storeId,
                                                   @Valid @RequestBody StoreUpdateReqDto updateDto) {
        StoreResDto updatedStore = storeService.updateStore(storeId, updateDto);
        return ResponseEntity.ok(updatedStore);
    }


    /**
     * 가게 삭제 (MASTER)
     */
    @DeleteMapping("/v1/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }


    /**
     * 삭제된 가게 조회 (is_delete = Ture)
     * */
    @GetMapping("/v1/stores/deleted")
    public ResponseEntity<List<Store>> getDeletedStores() {
        List<Store> deletedStores = storeService.getDeletedStores();
        return ResponseEntity.ok(deletedStores);
    }
    /**
     * 카테고리별 가게 조회 (ALL)
     */
    @GetMapping("/v1/stores/category")
    public ResponseEntity<List<StoreResDto>> getStoresByCategory(@RequestParam String category) {
        List<StoreResDto> result = storeService.getStoresByCategory(category);
        return ResponseEntity.ok(result);
    }
    /**
     * 가게 이름으로 검색 (ALL)
     */
    @GetMapping("/v1/stores/search")
    public ResponseEntity<List<StoreResDto>> searchStoresByName(@RequestParam String keyword) {
        List<StoreResDto> result = storeService.searchStoresByName(keyword);
        return ResponseEntity.ok(result);
    }
    /**
     * 가게 상태 변경 (ADMIN, MASTER, OWNER)
     */
    @PatchMapping("/owner/v1/stores/{storeId}/status")
    public ResponseEntity<StoreResDto> updateStoreStatus(@PathVariable UUID storeId,
                                                         @RequestBody Map<String, String> requestBody) {
        String statusStr = requestBody.get("isOpen");
        StoreResDto updatedStore = storeService.updateStoreStatus(storeId, statusStr);
        return ResponseEntity.ok(updatedStore);
    }

    /**
     * 가게 카테고리 변경 (ADMIN, MASTER, OWNER)
     */
    @PatchMapping("/owner/v1/stores/{storeId}/category")
    public ResponseEntity<StoreResDto> updateStoreCategory(@PathVariable UUID storeId,
                                                           @RequestBody Map<String, String> requestBody) {
        String categoryStr = requestBody.get("category");
        StoreResDto updatedStore = storeService.updateStoreCategory(storeId, categoryStr);
        return ResponseEntity.ok(updatedStore);
    }

}












