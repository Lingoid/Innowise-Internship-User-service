package com.innowise.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private Long id;
    private Long userId;
    private String number;
    private String holder;
    private LocalDate expiration_date;
}
