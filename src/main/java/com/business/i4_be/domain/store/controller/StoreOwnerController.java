package com.business.i4_be.domain.store.controller;

import com.business.i4_be.domain.order.dto.response.OrderResDto;
import com.business.i4_be.domain.store.dto.*;
import com.business.i4_be.domain.store.service.StoreService;
import com.business.i4_be.domain.user.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/owner/v1/stores")
@RequiredArgsConstructor
public class StoreOwnerController {
    private final StoreService storeService;

    //가게 등록
//    @PreAuthorize("hasAnyRole('ADMIN','MASTER')")
    @PostMapping
    public ResponseEntity<StoreResDto> createStore(@Valid @RequestBody StoreReqDto reqDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto newStore = storeService.createStore(reqDto, userDetails.getUser().getId());
        return ResponseEntity.ok(newStore);

    }
    //MASTER가 가게등록 후 OWNERID 수정
    @PatchMapping("{storeId}/change-owner")
    public ResponseEntity<StoreResDto> changeStoreOwner(
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreOwnerUpdateReqDto reqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreResDto updatedStore = storeService.updateStoreOwner(
                storeId, userDetails.getUser().getId(), reqDto.getNewUserId());

        return ResponseEntity.ok(updatedStore);
    }

    //가게 조회(단건)
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResDto> getStoreForOwner(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto store = storeService.getStoreForOwner(storeId,userDetails.getUser().getId());
        return ResponseEntity.ok(store);
    }

    //가게 목록 조회
    @GetMapping
    public ResponseEntity<PagedResDto<StoreResDto>> getAllStoresForOwner(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<StoreResDto> stores = storeService.getAllStoresForOwner(userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(new PagedResDto<>(stores));
    }
    //가게 수정 (이름 변경 불가)
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResDto> updateStore(@PathVariable UUID storeId,
                                                   @Validated(StoreReqDto.Update.class) @RequestBody StoreReqDto updateReqDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto updatedStore = storeService.updateStore(storeId, updateReqDto, userDetails.getUser().getId());
        return ResponseEntity.ok(updatedStore);
    }


    //가게 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        storeService.deleteStore(storeId, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    //가게 상태변경(OPEN, CLOSE)
    @PatchMapping("/{storeId}/status")
    public ResponseEntity<StoreResDto> updateStoreStatus(
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreStatusUpdateReqDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto updatedStore = storeService.updateStoreStatus(storeId, requestDto, userDetails.getUser().getId());
        return ResponseEntity.ok(updatedStore);
    }

    //가게 카테고리 변경
    @PatchMapping("/{storeId}/category")
    public ResponseEntity<StoreResDto> updateStoreCategory(
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreCategoryUpdateReqDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResDto updatedStore = storeService.updateStoreCategory(storeId, requestDto,userDetails.getUser().getId());
        return ResponseEntity.ok(updatedStore);
    }

    //특정가게의 주문 내역 조회(OWNER용)
    @GetMapping("/{storeId}/orders")
    public ResponseEntity<List<OrderResDto>> getOrdersByStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResDto> orders = storeService.getOrdersByStore(storeId, userDetails.getUser().getId());
        return ResponseEntity.ok(orders);
    }
}
