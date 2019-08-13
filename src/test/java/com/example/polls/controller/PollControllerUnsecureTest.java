package com.example.polls.controller;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import com.example.polls.payload.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PollController.class, secure = false)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class PollControllerUnsecureTest {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

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
    public void createPoll() throws Exception {
        Choice choice = Choice.builder().id("id").text("text").build();
        List<Choice> choices = new ArrayList<>();
        choices.add(choice);

        Poll poll = Poll.builder()
                .id("qwerty")
                .question("question")
                .choices(choices)
                .build();

        PollRequest pollRequest = new PollRequest();
        pollRequest.setQuestion("question");

        PollLength pollLength = new PollLength();
        pollLength.setDays(2);
        pollLength.setHours(12);
        pollRequest.setPollLength(pollLength);

        ChoiceRequest choiceRequest1 = new ChoiceRequest();
        choiceRequest1.setText("choice1");

        ChoiceRequest choiceRequest2 = new ChoiceRequest();
        choiceRequest2.setText("choice2");

        List<ChoiceRequest> choiceRequests = new ArrayList<>();
        choiceRequests.add(choiceRequest1);
        choiceRequests.add(choiceRequest2);
        pollRequest.setChoices(choiceRequests);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(pollRequest);

        given(pollService.createPoll(any(PollRequest.class))).willReturn(poll);

        MvcResult result = mockMvc.perform(
                post("http://testserver/api/polls")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("Location", "http://testserver/api/polls/qwerty"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Poll Created Successfully"))
                .andReturn();

        verify(pollService, times(1)).createPoll(any(PollRequest.class));
        verifyNoMoreInteractions(this.pollService);
    }

    @Test
    public void getPollById() throws Exception {
        String pollId = "pollId";
        String question = "question";
        PollResponse pollResponse = PollResponse.builder().id(pollId).question(question).build();

        UserPrincipal userPrincipal = new UserPrincipal("id", "name", "username",
                "name@email.com",
                "password",
                new ArrayList<>());
        given(authenticationService.getUserPrincipal()).willReturn(userPrincipal);
        given(pollService.getPollById(eq(pollId), any(UserPrincipal.class))).willReturn(pollResponse);

        MvcResult result = mockMvc.perform(
                get("http://testserver/api/polls/" + pollId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pollId))
                .andExpect(jsonPath("$.question").value(question))
                .andReturn();

        verify(authenticationService, times(1)).getUserPrincipal();
        verifyNoMoreInteractions(authenticationService);
        verify(pollService, times(1)).getPollById(eq(pollId), any(UserPrincipal.class));
        verifyNoMoreInteractions(pollService);
    }

    @Test
    public void castVoteAndGetUpdatedPoll() throws Exception {
        String pollId = "pollId";
        String question = "question";
        PollResponse pollResponse = PollResponse.builder()
                .id(pollId)
                .question(question)
                .totalVotes(1l)
                .build();

        UserPrincipal userPrincipal = new UserPrincipal("id", "name", "username",
                "name@email.com",
                "password",
                new ArrayList<>());

        VoteRequest voteRequest = VoteRequest.builder().choiceId("choiceId").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(voteRequest);
        logger.info("content : " + content);

        given(authenticationService.getUserPrincipal()).willReturn(userPrincipal);
        given(pollService.castVoteAndGetUpdatedPoll(eq(pollId), any(VoteRequest.class), any(UserPrincipal.class)))
                .willReturn(pollResponse);

        mockMvc.perform(
                post("/api/polls/" + pollId + "/votes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pollId))
                .andExpect(jsonPath("$.question").value(question))
                .andExpect(jsonPath("$.totalVotes").value(1l))
                .andReturn();
    }
}