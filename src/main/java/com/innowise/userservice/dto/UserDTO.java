package com.innowise.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 50, message = "name should be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Surname must not be blank")
    @Size(min = 2, max = 50, message = "surname should be between 2 and 50 characters")
    private String surname;

    @NotNull(message = "Birth date must not be null")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;
}
