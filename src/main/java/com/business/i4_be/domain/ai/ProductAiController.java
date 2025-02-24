package com.business.i4_be.domain.ai;

import com.business.i4_be.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductAiController {
    private final AiDescriptionService aiDescriptionService;

    @PostMapping("/ai-description")
    public ResponseEntity<String> getAiGeneratedDescription(
            @RequestParam String productName,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String aiDescription = aiDescriptionService.generateAndSaveDescription(userDetails.getUser().getId(), productName);
        return ResponseEntity.ok(aiDescription);
    }

    @GetMapping("/ai-description/my")
    public ResponseEntity<List<AiDescriptionDto>> getMyAiDescriptions(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AiDescriptionDto> aiDescriptions = aiDescriptionService.getUserDescriptions(userDetails.getUser().getId());
        return ResponseEntity.ok(aiDescriptions);
    }
}
