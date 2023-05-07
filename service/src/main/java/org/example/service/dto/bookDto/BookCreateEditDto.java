package org.example.service.dto.bookDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class BookCreateEditDto {

    @NotEmpty(message = "Title must be not empty")
    @Size(min = 2, max = 50, message = "Title must contains from 2 to 50 symbols")
    String title;

    Long authorId;

    Integer categoryId;

    @Min(value = 1800, message = "Publish year must be from 1800 to current year")
    Integer publishYear;

    @NotEmpty(message = "Description must be not empty")
    String description;

    @Positive(message = "Number must be positive")
    Integer number;

    MultipartFile picture;
}
