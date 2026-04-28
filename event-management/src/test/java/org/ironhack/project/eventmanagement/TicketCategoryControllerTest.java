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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
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
        request.setEventId(1L);

        given(ticketCategoryService.create(any(CreateTicketCategoryRequest.class)))
                .willReturn(ticketCategory);

        given(ticketCategoryMapper.toResponse(ticketCategory))
                .willReturn(response);

        mockMvc.perform(post("/api/ticket-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(ticketCategoryService).create(any(CreateTicketCategoryRequest.class));
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void getById_success() throws Exception {

        TicketCategory ticketCategory = new TicketCategory();
        TicketCategoryResponse response = new TicketCategoryResponse();

        given(ticketCategoryService.getById(1L)).willReturn(ticketCategory);
        given(ticketCategoryMapper.toResponse(ticketCategory)).willReturn(response);

        mockMvc.perform(get("/api/ticket-categories/1"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).getById(1L);
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void getAll_success() throws Exception {

        TicketCategory ticketCategory = new TicketCategory();
        TicketCategoryResponse response = new TicketCategoryResponse();

        given(ticketCategoryService.getAll()).willReturn(List.of(ticketCategory));
        given(ticketCategoryMapper.toResponse(ticketCategory)).willReturn(response);

        mockMvc.perform(get("/api/ticket-categories"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).getAll();
        verify(ticketCategoryMapper).toResponse(ticketCategory);
    }

    @Test
    void delete_success() throws Exception {

        mockMvc.perform(delete("/api/ticket-categories/1"))
                .andExpect(status().isOk());

        verify(ticketCategoryService).delete(1L);
    }
}