package com.example.polls.payload;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class ChoiceResponse {
    private String id;
    private String text;
    private long voteCount;
}
