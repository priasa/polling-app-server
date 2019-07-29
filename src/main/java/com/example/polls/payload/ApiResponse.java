package com.example.polls.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiResponse {
    private Boolean success;
    private String message;
}
