package com.business.i4_be.domain.store.controller;

import com.business.i4_be.domain.store.dto.StoreReqDto;
import com.business.i4_be.domain.store.dto.StoreUpdateReqDto;
import com.business.i4_be.domain.store.dto.StoreListResDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
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

    //가게 조회(목록)
    @GetMapping("/v1/stores")
    public ResponseEntity<StoreListResDto> getAllStores() {
        StoreListResDto stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    //가게 수정
    @PutMapping("/owner/v1/stores/{storeId}")
    public ResponseEntity<StoreResDto> updateStore(@PathVariable UUID storeId,
                                                   @Valid @RequestBody StoreUpdateReqDto updateReqDto) {
        StoreResDto updatedStore = storeService.updateStore(storeId, updateReqDto);
        return ResponseEntity.ok(updatedStore);
    }


    //가게 삭제
    @DeleteMapping("/v1/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }

    //가게 검색(카테고리)
    @GetMapping("/v1/stores/category")
    public ResponseEntity<List<StoreResDto>> getStoresByCategory(@RequestParam String category) {
        List<StoreResDto> stores = storeService.getStoresByCategory(category);
        return ResponseEntity.ok(stores);
    }
    //가게 검색(이름)
    @GetMapping("/v1/stores/search")
    public ResponseEntity<List<StoreResDto>> searchStoresByName(@RequestParam String keyword) {
        List<StoreResDto> stores = storeService.searchStoresByName(keyword);
        return ResponseEntity.ok(stores);
    }
    //가게 변경(상태만)
    @PatchMapping("/owner/v1/stores/{storeId}/status")
    public ResponseEntity<StoreResDto> updateStoreStatus(@PathVariable UUID storeId,
                                                         @RequestBody Map<String, String> requestBody) {
        String isOpen = requestBody.get("isOpen");
        StoreResDto updatedStore = storeService.updateStoreStatus(storeId, isOpen);
        return ResponseEntity.ok(updatedStore);
    }

    //가게 변경(카테고리만)
    @PatchMapping("/owner/v1/stores/{storeId}/category")
    public ResponseEntity<StoreResDto> updateStoreCategory(@PathVariable UUID storeId,
                                                           @RequestBody Map<String, String> requestBody) {
        String categoryStr = requestBody.get("category");
        StoreResDto updatedStore = storeService.updateStoreCategory(storeId, categoryStr);
        return ResponseEntity.ok(updatedStore);
    }
}












