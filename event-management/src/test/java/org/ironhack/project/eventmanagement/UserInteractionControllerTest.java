package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.service.user.UserInteractionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserInteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInteractionService userInteractionService;


    @Test
    void favoriteEvent_success() throws Exception {

        mockMvc.perform(post("/api/users/me/favorites/1"))
                .andExpect(status().isOk());

        verify(userInteractionService).favoriteEvent(1L);
    }


    @Test
    void unfavoriteEvent_success() throws Exception {

        mockMvc.perform(delete("/api/users/me/favorites/1"))
                .andExpect(status().isOk());

        verify(userInteractionService).unfavoriteEvent(1L);
    }


    @Test
    void getMyFavoriteEvents_success() throws Exception {

        Page<EventResponse> page = new PageImpl<>(List.of());

        given(userInteractionService.getMyFavoriteEvents(any(PageRequest.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/users/me/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(userInteractionService).getMyFavoriteEvents(any(PageRequest.class));
    }


    @Test
    void followVendor_success() throws Exception {

        mockMvc.perform(post("/api/users/me/following/1"))
                .andExpect(status().isOk());

        verify(userInteractionService).followVendor(1L);
    }


    @Test
    void unfollowVendor_success() throws Exception {

        mockMvc.perform(delete("/api/users/me/following/1"))
                .andExpect(status().isOk());

        verify(userInteractionService).unfollowVendor(1L);
    }


    @Test
    void getMyFollowedVendors_success() throws Exception {

        Page<VendorResponse> page = new PageImpl<>(List.of());

        given(userInteractionService.getMyFollowedVendors(any(PageRequest.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/users/me/following"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(userInteractionService).getMyFollowedVendors(any(PageRequest.class));
    }
}