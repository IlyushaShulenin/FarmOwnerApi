package ru.shulenin.farmownerapi.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.service.WorkerService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "ilya.shulenin36@gmail.com", password = "owner")
class WorkerRestControllerTest extends TestBase {
    private final static String APP_NAME = "/owner-api/v1/";

    private final WorkerService service;
    private final MockMvc mockMvc;
    private Gson gson = new Gson();

    private String getUri(String path) {
        return APP_NAME + path;
    }

    @Test
    void findAll() throws Exception {
        var all = gson.toJson(service.findAll());

        mockMvc.perform(get(getUri("worker")))
                .andExpect(status().isOk())
                .andExpect(content().json(all));
    }

    @Test
    void findById() throws Exception {
        var found = gson.toJson(service.findById(1L).get());

        mockMvc.perform(get(getUri("worker/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(found));

        mockMvc.perform(get(getUri("worker/100")))
                .andExpect(status().isNotFound());
    }
}