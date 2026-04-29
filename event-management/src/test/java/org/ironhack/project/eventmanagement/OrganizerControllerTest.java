package org.ironhack.project.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;
import org.ironhack.project.eventmanagement.entity.OrganizerRole;
import org.ironhack.project.eventmanagement.service.organizer.EventOrganizerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrganizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventOrganizerService organizerService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrganizerResponse mockOrganizer() {
        return new OrganizerResponse(
                1L,
                2L,
                "Test Vendor",
                OrganizerRole.MAIN
        );
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void addOrganizer_success() throws Exception {

        given(organizerService.addOrganizer(eq(1L), any(AddOrganizerRequest.class)))
                .willReturn(mockOrganizer());

        mockMvc.perform(post("/events/1/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "vendorId": 2,
                      "role": "MAIN"
                    }
                    """))
                .andExpect(status().isOk());

        verify(organizerService).addOrganizer(eq(1L), any(AddOrganizerRequest.class));
    }


    @Test
    void getOrganizers_success() throws Exception {

        given(organizerService.getOrganizers(1L))
                .willReturn(List.of(mockOrganizer()));

        mockMvc.perform(get("/events/1/organizers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(organizerService).getOrganizers(1L);
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void deleteOrganizer_success() throws Exception {

        mockMvc.perform(delete("/events/1/organizers/5"))
                .andExpect(status().isOk());

        verify(organizerService).removeOrganizer(5L);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void transferOwnership_success() throws Exception {

        mockMvc.perform(patch("/events/1/organizers/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "newVendorId": 10
                        }
                        """))
                .andExpect(status().isOk());

        verify(organizerService).transferOwnership(1L, 10L);
    }
}