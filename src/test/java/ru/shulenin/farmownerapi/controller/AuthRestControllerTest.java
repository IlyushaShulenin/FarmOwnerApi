package ru.shulenin.farmownerapi.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.shulenin.farmownerapi.TestBase;
import ru.shulenin.farmownerapi.annotation.IntegrationTest;
import ru.shulenin.farmownerapi.dto.SignInRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthRestControllerTest extends TestBase {
    private final static String APP_NAME = "/auth/sign-in";
    private final static String WRONG_USERNAME = "unregistered@mail.com";
    private final static String PASSWORD = "owner";


    private final MockMvc mockMvc;
    private Gson gson = new Gson();

    @Contract(pure = true)
    private String getUri(String path) {
        return APP_NAME + path;
    }

    @Test
    @WithMockUser(username = WRONG_USERNAME, password = PASSWORD)
    void wrongSignIn() throws Exception {
        var request = new SignInRequest(
            WRONG_USERNAME,
            PASSWORD
        );

        mockMvc.perform(post(APP_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isForbidden());
    }
}