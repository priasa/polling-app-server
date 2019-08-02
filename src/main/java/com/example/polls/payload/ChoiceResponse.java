package com.example.polls.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChoiceResponse {
    private String id;
    private String text;
    private long voteCount;
}
