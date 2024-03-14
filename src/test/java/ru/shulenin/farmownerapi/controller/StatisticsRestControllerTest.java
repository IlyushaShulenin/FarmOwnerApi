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
import ru.shulenin.farmownerapi.service.ReportService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "ilya.shulenin36@gmail.com", password = "owner")
class StatisticsRestControllerTest extends TestBase {
    private final static String APP_NAME = "/owner-api/v1/";

    private final ReportService service;
    private final MockMvc mockMvc;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();


    private String getUri(String path) {
        return APP_NAME + path;
    }

    @Test
    void getProductivity() throws Exception {
        var statForFirst = gson.toJson(service.getProductivity(1L));

        mockMvc.perform(get(getUri("statistics/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(statForFirst));

        var statForFirstNyMonth = gson.toJson(service.getProductivity(1L, 10));

        mockMvc.perform(get(getUri("statistics/1"))
                        .param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(statForFirstNyMonth));
    }
}