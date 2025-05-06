package com.example.user_subscriptions_service.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.user_subscriptions_service.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserSubscriptionIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testFullUserLifecycle() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Dmitry", "email":"dmitry@example.com"}
                        """))
            .andExpect(status().isCreated())
            .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Integer userIdInteger = JsonPath.read(responseJson, "$.id");
        long userId = userIdInteger.longValue();

        mockMvc.perform(post("/users/" + userId + "/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title":"Netflix"}
                        """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/users/" + userId + "/subscriptions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

}
