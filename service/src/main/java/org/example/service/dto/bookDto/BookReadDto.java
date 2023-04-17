package org.example.service.dto.bookDto;

import lombok.Value;

@Value
public class BookReadDto {
    Long id;
    String title;
    CategoryReadDto category;
    Integer publishYear;
    String description;
    Integer number;
    AuthorReadDto author;
    String picture;
}
