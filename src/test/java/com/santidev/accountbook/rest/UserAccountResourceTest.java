package com.santidev.accountbook.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santidev.accountbook.model.UserAccount;
import com.santidev.accountbook.repository.BalanceRepository;
import com.santidev.accountbook.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAccountResource.class)
public class UserAccountResourceTest {

    public static final String NAME_TEST = "Test test";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private BalanceRepository balanceRepository;

    static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void postAndGetTest() throws Exception {
        // Given
        when(userAccountRepository.save(any())).then(inv -> getNewAccount());
        when(userAccountRepository.findById("2")).thenReturn(Optional.of(getNewAccount()));

        // When
        mvc.perform(post("/api/user-accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getNewAccountWithOutId())))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test test"))
                .andExpect(jsonPath("$.id").value("2"));

        //Then
        mvc.perform(get("/api/user-accounts/2").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(NAME_TEST))
                .andExpect(jsonPath("$.id").value("2"));
    }

    @Test
    void getAllTest() throws Exception {
        // Given
        List<UserAccount> userAccounts = Arrays.asList(getNewAccount(), getNewAccount());
        when(userAccountRepository.findAll()).thenReturn(userAccounts);

        // When
        mvc.perform(get("/api/user-accounts").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(NAME_TEST))
                .andExpect(jsonPath("$[1].name").value(NAME_TEST))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(userAccounts)));

        verify(userAccountRepository).findAll();
    }

    private UserAccount getNewAccount() {
        return UserAccount.builder().id("2").name("Test test").disabled(Boolean.FALSE).build();
    }

    private UserAccount getNewAccountWithOutId() {
        return UserAccount.builder().name("Test test").disabled(Boolean.FALSE).build();
    }
}
