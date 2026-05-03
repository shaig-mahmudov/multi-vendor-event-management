package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.request.user.ChangePasswordRequest;
import org.ironhack.project.eventmanagement.dto.request.user.UpdateUserRequest;
import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void me_success() throws Exception {

        UserResponse response = new UserResponse();

        given(userService.getMe()).willReturn(response);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());

        verify(userService).getMe();
    }


    @Test
    void updateMe_success() throws Exception {

        UserResponse response = new UserResponse();

        given(userService.updateMe(any(UpdateUserRequest.class)))
                .willReturn(response);

        mockMvc.perform(patch("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "New Name"
                        }
                        """))
                .andExpect(status().isOk());

        verify(userService).updateMe(any(UpdateUserRequest.class));
    }


    @Test
    void changePassword_success() throws Exception {

        mockMvc.perform(patch("/api/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "currentPassword": "old12345",
                          "newPassword": "new123456"
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(userService).changeMyPassword(any(ChangePasswordRequest.class));
    }
}