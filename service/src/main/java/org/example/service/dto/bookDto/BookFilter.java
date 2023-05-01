package org.example.service.dto.bookDto;

import lombok.Builder;

@Builder
public record BookFilter(Integer publishYear,
                         String category,
                         String author,
                         String title) {
}
