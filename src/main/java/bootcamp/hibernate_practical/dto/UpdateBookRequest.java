package bootcamp.hibernate_practical.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookRequest {
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    private String author;

    @NotBlank(message = "Genre cannot be empty")
    private String genre;

    @Min(value = 1, message = "Publication year must be positive")
    private int publicationYear;

    @NotBlank(message = "Availability must be specified")
    private Boolean available;
}
