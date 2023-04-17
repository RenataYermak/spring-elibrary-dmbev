package org.example.service.dto.bookDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
@FieldNameConstants
public class BookCreateEditDto {

    @NotEmpty
    @Size(min = 2, max = 50)
    String title;

    @NotEmpty
    Long authorId;

    @NotEmpty
    Integer categoryId;

    Integer publishYear;

    @NotEmpty
    String description;

    Integer number;

    @Size(max = 128)
    MultipartFile picture;
}
