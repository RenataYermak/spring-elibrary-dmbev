package org.example.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookFilter {

    Integer publishYear;
    String category;
    String author;
}
