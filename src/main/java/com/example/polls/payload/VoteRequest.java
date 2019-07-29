package com.example.polls.payload;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Data
public class VoteRequest {
    @NotNull
    private Long choiceId;
}

