package com.example.polls.controller;

import com.example.polls.model.Poll;
import com.example.polls.payload.*;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AuthenticationService;
import com.example.polls.service.PollService;
import com.example.polls.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public ResponseEntity<?>  getPolls(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        UserPrincipal userPrincipal = authenticationService.getUserPrincipal();
        PagedResponse<PollResponse> response = pollService.getAllPolls(userPrincipal, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }


    @GetMapping("/{pollId}")
    public PollResponse getPollById(Authentication authentication, @PathVariable String pollId) {
        return pollService.getPollById(pollId, (UserPrincipal)authentication.getPrincipal());
    }

    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(Authentication authentication, @PathVariable String pollId,
                         @Valid @RequestBody VoteRequest voteRequest) {
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, (UserPrincipal) authentication.getPrincipal());
    }

}
