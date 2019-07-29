package com.example.polls.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PollResponse {
    private String id;
    private String question;
    private List<ChoiceResponse> choices;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Instant expirationDateTime;
    private Boolean expired;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String selectedChoice;
    private Long totalVotes;
}
