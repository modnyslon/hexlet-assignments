package exercise.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// BEGIN
@Getter
@Setter
public class GuestCreateDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[0-9]{11,13}$",
            message = "Phone number must start with + and contain 11-13 digits")
    private String phoneNumber;

    @NotBlank(message = "Club card number is required")
    @Pattern(regexp = "^[0-9]{4}$",
            message = "Club card must contain exactly 4 digits")
    private String clubCard;

    @NotNull(message = "Card expiration date is required")
    @FutureOrPresent(message = "Card must not be expired")
    private LocalDate cardValidUntil;
}
// END
