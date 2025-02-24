package com.business.i4_be.domain.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResDto {
    @JsonProperty("candidates")
    private List<Candidate> candidates;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        @JsonProperty("content")
        private Content content;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        @JsonProperty("parts")
        private List<Part> parts;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        @JsonProperty("text")
        private String text;
    }

    public String extractText() {
        return candidates != null && !candidates.isEmpty()
                ? candidates.get(0).content.parts.get(0).text
                : "추천 설명을 생성할 수 없습니다.";
    }
}
