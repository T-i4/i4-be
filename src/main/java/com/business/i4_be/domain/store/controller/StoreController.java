package com.business.i4_be.domain.store.controller;

import com.business.i4_be.domain.store.dto.PagedResDto;
import com.business.i4_be.domain.store.dto.StoreResDto;
import com.business.i4_be.domain.store.service.StoreService;

import com.business.i4_be.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;


    //가게 조회(단건)
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResDto> getStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto store = storeService.getStore(storeId,userDetails.getUser().getId());
        return ResponseEntity.ok(store);
    }


    //가게 목록 조회
    @GetMapping
    public ResponseEntity<PagedResDto<StoreResDto>> getAllStores(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        Page<StoreResDto> stores = storeService.getAllStores(userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(new PagedResDto<>(stores));
    }
    //가게 검색(이름)
    @GetMapping("/search")
    public ResponseEntity<PagedResDto<StoreResDto>> searchStoresByName(
            @RequestParam String keyword,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<StoreResDto> stores = storeService.searchStoresByName(keyword, userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(new PagedResDto<>(stores));
    }

    //가게 검색(카테고리)
    @GetMapping("/category")
    public ResponseEntity<PagedResDto<StoreResDto>> getStoresByCategory(
            @RequestParam String category,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<StoreResDto> stores = storeService.getStoresByCategory(category, userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(new PagedResDto<>(stores));
    }


}












