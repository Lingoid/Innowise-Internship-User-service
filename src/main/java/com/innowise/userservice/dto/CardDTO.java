package com.innowise.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CardDTO {
    private Long id;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotBlank(message = "Card number must not be blank")
    @Pattern(regexp = "\\d{12}", message = "Card number must contain 12 digits")
    private String number;

    @NotBlank(message = "Holder must not be blank")
    @Size(max = 50, message = "Holder name should not exceed 50 characters")
    private String holder;

    @NotNull(message = "Expiration date must not be null")
    private LocalDate expirationDate;
}
