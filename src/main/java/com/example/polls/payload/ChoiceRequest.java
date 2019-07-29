package com.example.polls.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Data
public class ChoiceRequest {
    @NotBlank
    @Size(max = 40)
    private String text;
}
