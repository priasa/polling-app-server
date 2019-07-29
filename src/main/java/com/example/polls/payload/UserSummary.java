package com.example.polls.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSummary {
    private String id;
    private String username;
    private String name;
}
