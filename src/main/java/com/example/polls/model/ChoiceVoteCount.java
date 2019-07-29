package com.example.polls.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ChoiceVoteCount {
    private String choiceId;
    private Long voteCount;
}

