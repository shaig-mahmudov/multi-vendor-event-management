package org.ironhack.project.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.service.event.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "VENDOR")
    void create_success() throws Exception {

        EventResponse response = mockEventResponse();

        given(eventService.create(any(CreateEventRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "title": "Test Event",
                          "description": "desc",
                          "date": "2026-05-01T10:00:00",
                          "location": "Baku",
                          "imageUrl": "img",
                          "categoryId": 1
                        }
                        """))
                .andExpect(status().isCreated());

        verify(eventService).create(any(CreateEventRequest.class));
    }


    @Test
    void getById_success() throws Exception {

        given(eventService.getById(1L))
                .willReturn(mockEventResponse());

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk());

        verify(eventService).getById(1L);
    }


    @Test
    void getAllPublished_success() throws Exception {

        given(eventService.getAllPublished(null, null, null))
                .willReturn(List.of(mockEventResponse()));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());

        verify(eventService).getAllPublished(null, null, null);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_success() throws Exception {

        given(eventService.getAll())
                .willReturn(List.of(mockEventResponse()));

        mockMvc.perform(get("/events/all"))
                .andExpect(status().isOk());

        verify(eventService).getAll();
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void update_success() throws Exception {

        given(eventService.update(eq(1L), any(UpdateEventRequest.class)))
                .willReturn(mockEventResponse());

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "title": "Updated",
                          "description": "desc",
                          "date": "2026-05-01T10:00:00",
                          "location": "Baku",
                          "imageUrl": "img",
                          "categoryId": 1
                        }
                        """))
                .andExpect(status().isOk());

        verify(eventService).update(eq(1L), any(UpdateEventRequest.class));
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void delete_success() throws Exception {

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        verify(eventService).delete(1L);
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void publish_success() throws Exception {

        given(eventService.publish(1L))
                .willReturn(mockEventResponse());

        mockMvc.perform(patch("/events/1/publish"))
                .andExpect(status().isOk());

        verify(eventService).publish(1L);
    }


    private EventResponse mockEventResponse() {
        return new EventResponse(
                1L,
                "Test",
                "Desc",
                LocalDateTime.now(),
                "Baku",
                "img",
                null,
                1L,
                "Category",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}