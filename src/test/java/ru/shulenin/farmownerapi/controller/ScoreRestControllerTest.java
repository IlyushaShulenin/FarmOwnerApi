package ru.shulenin.farmownerapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.controller.adapter.LocalDateTypeAdapter;
import ru.shulenin.farmownerapi.service.ScoreService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "ilya.shulenin36@gmail.com", password = "owner")
class ScoreRestControllerTest extends TestBase {
    private final static String APP_NAME = "/owner-api/v1/";

    private final ScoreService service;
    private final MockMvc mockMvc;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    private String getUri(String path) {
        return APP_NAME + path;
    }

    @Test
    void findAll() throws Exception {
        var all = gson.toJson(service.findAll());

        mockMvc.perform(get(getUri("score")))
                .andExpect(status().isOk())
                .andExpect(content().json(all));
    }

    @Test
    void findById() throws Exception {
        var found = gson.toJson(service.findById(1L).get());

        mockMvc.perform(get(getUri("score/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(found));

        mockMvc.perform(get(getUri("score/100")))
                .andExpect(status().isNotFound());
    }
}