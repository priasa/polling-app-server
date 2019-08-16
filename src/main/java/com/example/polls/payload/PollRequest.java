package com.example.polls.payload;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PollRequest {
    @NotBlank
    @Size(max = 140)
    private String question;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    @Builder.Default
    private List<ChoiceRequest> choices = new ArrayList<>();

    @NotNull
    @Valid
    private PollLength pollLength;
}
