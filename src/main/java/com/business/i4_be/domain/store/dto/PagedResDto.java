package com.business.i4_be.domain.store.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagedResDto<T> {
    private final List<T> content;
    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final int pageNumber;
    private final int numberOfElements;
    private final boolean first;
    private final boolean last;

    public PagedResDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.pageNumber = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
