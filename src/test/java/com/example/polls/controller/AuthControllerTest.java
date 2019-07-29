package com.example.polls.controller;

import com.example.polls.model.Role;
import com.example.polls.model.RoleName;
import com.example.polls.model.User;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, secure = false)
@RunWith(SpringRunner.class)
public class AuthControllerTest {

    @MockBean
    JwtTokenProvider tokenProvider;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String generatedToken;

    @Before
    public void setUp() throws Exception {
        generatedToken = "generatedToken";
        given(tokenProvider.generateToken(any())).willReturn(generatedToken);
    }

    @Test
    public void authenticateUser() throws Exception {
        mockMvc.perform(post("/api/auth/signin").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(generatedToken))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));


        verify(tokenProvider, times(1)).generateToken(any());
        verifyNoMoreInteractions(this.tokenProvider);
    }

    @Test
    public void registerUserExistsByUsernameTrue() throws Exception {
        given(userRepository.existsByUsername(anyString())).willReturn(Boolean.TRUE);

        SignUpRequest signUpRequest = SignUpRequest.builder().name("name").email("email").password("password")
                .username("username")
                .build();

        String content = "{\"name\":\"rabia anindita\",\"username\":\"rabia\",\"email\":\"rabia.anindita@gmail.com\"," +
                "\"password\":\"3nengtri\"}";
        MvcResult result = mockMvc.perform(
                post("/api/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username is already taken!"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(userRepository, times(1)).existsByUsername(any());
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void registerUserExistsByEmailTrue() throws Exception {
        given(userRepository.existsByUsername(anyString())).willReturn(Boolean.FALSE);
        given(userRepository.existsByEmail(anyString())).willReturn(Boolean.TRUE);

        SignUpRequest signUpRequest = SignUpRequest.builder().name("name").email("email").password("password")
                .username("username")
                .build();

        String content = "{\"name\":\"rabia anindita\",\"username\":\"rabia\",\"email\":\"rabia.anindita@gmail.com\"," +
                "\"password\":\"3nengtri\"}";
        MvcResult result = mockMvc.perform(
                post("/api/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email Address already in use!"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(userRepository, times(1)).existsByUsername(any());
        verify(userRepository, times(1)).existsByEmail(any());
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void registerUserUserRoleNotExists() throws Exception {
        given(userRepository.existsByUsername(anyString())).willReturn(Boolean.FALSE);
        given(userRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.empty());

        SignUpRequest signUpRequest = SignUpRequest.builder().name("name").email("email").password("password")
                .username("username")
                .build();

        String content = "{\"name\":\"rabia anindita\",\"username\":\"rabia\",\"email\":\"rabia.anindita@gmail.com\"," +
                "\"password\":\"3nengtri\"}";
        MvcResult result = mockMvc.perform(
                post("/api/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());

        verify(userRepository, times(1)).existsByUsername(any());
        verify(userRepository, times(1)).existsByEmail(any());
        verifyNoMoreInteractions(this.userRepository);

        verify(roleRepository, times(1)).findByName(any());
        verifyNoMoreInteractions(this.roleRepository);
    }

    @Test
    public void registerUser() throws Exception {
        given(userRepository.existsByUsername(anyString())).willReturn(Boolean.FALSE);
        given(userRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.of(
                Role.builder()
                        .id(UUID.randomUUID().toString())
                        .name(RoleName.ROLE_USER)
                        .build()));

        SignUpRequest signUpRequest = SignUpRequest.builder().name("rabia anindita").email("rabia.anindita@gmail.com")
                .password("3nengtri")
                .username("rabia")
                .build();
        given(userRepository.save(any(User.class))).willReturn(
                User.builder()
                        .email(signUpRequest.getEmail())
                        .password(signUpRequest.getPassword())
                        .username(signUpRequest.getUsername())
                        .name(signUpRequest.getName()).build());

        String content = "{\"name\":\"rabia anindita\",\"username\":\"rabia\",\"email\":\"rabia.anindita@gmail.com\"," +
                "\"password\":\"3nengtri\"}";
        MvcResult result = mockMvc.perform(
                post("/api/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(userRepository, times(1)).existsByUsername(any());
        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).save(any());
        verifyNoMoreInteractions(this.userRepository);

        verify(roleRepository, times(1)).findByName(any());
        verifyNoMoreInteractions(this.roleRepository);
    }
}