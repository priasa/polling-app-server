package com.example.polls.payload;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    @NotNull
    private String choiceId;
}

