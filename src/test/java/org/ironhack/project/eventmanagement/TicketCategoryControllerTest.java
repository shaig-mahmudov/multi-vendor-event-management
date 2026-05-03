package org.ironhack.project.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.ironhack.project.eventmanagement.mapper.TicketCategoryMapper;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class TicketCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketCategoryService ticketCategoryService;

    @MockBean
    private TicketCategoryMapper ticketCategoryMapper;

    @Test
    void create_success() throws Exception {

        TicketCategory ticketCategory = new TicketCategory();
        TicketCategoryResponse response = new TicketCategoryResponse();

        CreateTicketCategoryRequest request = new CreateTicketCategoryRequest();
        request.setName("VIP");
        request.setPrice(new BigDecimal("100"));
        request.setQuantity(50);

        given(ticketCategoryService.create(eq(1L), any(CreateTicketCategoryRequest.class)))
                .willReturn(ticketCategory);

        given(ticketCategoryMapper.toResponse(ticketCategory))
                .willReturn(response);

        mockMvc.perform(post("/events/1/ticket-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(ticketCategoryService).create(eq(1L), any(CreateTicketCategoryRequest.class));
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void getByEvent_success() throws Exception {

        TicketCategory ticketCategory = new TicketCategory();
        TicketCategoryResponse response = new TicketCategoryResponse();

        given(ticketCategoryService.getByEventId(1L)).willReturn(List.of(ticketCategory));
        given(ticketCategoryMapper.toResponse(ticketCategory)).willReturn(response);

        mockMvc.perform(get("/events/1/ticket-categories"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).getByEventId(1L);
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void getAll_success() throws Exception {

        TicketCategory ticketCategory = new TicketCategory();
        TicketCategoryResponse response = new TicketCategoryResponse();

        given(ticketCategoryService.getAll()).willReturn(List.of(ticketCategory));
        given(ticketCategoryMapper.toResponse(ticketCategory)).willReturn(response);

        mockMvc.perform(get("/ticket-categories/all"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).getAll();
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void delete_success() throws Exception {

        mockMvc.perform(delete("/events/1/ticket-categories/2"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).delete(1L, 2L);
    }
}