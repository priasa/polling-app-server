package com.example.polls.controller;

import com.example.polls.payload.ChoiceResponse;
import com.example.polls.payload.PagedResponse;
import com.example.polls.payload.PollResponse;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AuthenticationService;
import com.example.polls.service.PollService;
import com.example.polls.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PollController.class)
@RunWith(SpringRunner.class)
public class PollControllerTest {

    @MockBean
    private PollRepository pollRepository;

    @MockBean
    private VoteRepository voteRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PollService pollService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private PollResponse pollResponse;
    private List<ChoiceResponse> choiceResponseList = new ArrayList<>();
    private List<PollResponse> pollResponseList = new ArrayList<>();

    @Before
    public void setup(){
        choiceResponseList.add(ChoiceResponse.builder().id("id").text("text").build());
        pollResponse = PollResponse.builder().id("id").question("question").choices(choiceResponseList).build();
        pollResponseList.add(pollResponse);
    }

    @Test
    @WithMockUser(username = "myUser")
    public void getPolls() throws Exception {
        PagedResponse<PollResponse> pollPagedResponse = PagedResponse.<PollResponse>builder()
                .content(pollResponseList)
                .page(0)
                .size(30)
                .build();
        UserPrincipal userPrincipal = new UserPrincipal("id", "name", "username",
                "name@email.com",
                "password",
                new ArrayList<>());
        given(authenticationService.getUserPrincipal()).willReturn(userPrincipal);
        given(pollService.getAllPolls(any(UserPrincipal.class), eq(0), eq(30))).willReturn(pollPagedResponse);

        mockMvc.perform(get("/api/polls").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(AppConstants.DEFAULT_PAGE_NUMBER))
                .andExpect(jsonPath("$.size").value(AppConstants.DEFAULT_PAGE_SIZE))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());


        verify(pollService, times(1)).getAllPolls(any(UserPrincipal.class), anyInt(), anyInt());
        verifyNoMoreInteractions(this.pollService);
    }

    @Test
    public void getPolls_WithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/polls").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createPoll() {
    }

    @Test
    public void getPollById() {
    }

    @Test
    public void castVote() {
    }
}