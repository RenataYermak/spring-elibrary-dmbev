package org.example.service.dto;

import lombok.Builder;

@Builder
public record BookFilter(Integer publishYear,
                         String category,
                         String author) {
}
