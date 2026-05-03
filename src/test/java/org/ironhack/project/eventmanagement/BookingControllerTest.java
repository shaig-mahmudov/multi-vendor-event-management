package org.ironhack.project.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.eventmanagement.dto.request.booking.CreateBookingRequest;
import org.ironhack.project.eventmanagement.dto.response.BookingResponse;
import org.ironhack.project.eventmanagement.entity.Booking;
import org.ironhack.project.eventmanagement.mapper.BookingMapper;
import org.ironhack.project.eventmanagement.service.booking.BookingService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createBooking_success() throws Exception {

        Booking booking = new Booking();
        BookingResponse response = new BookingResponse();

        given(bookingService.createBooking(any(CreateBookingRequest.class)))
                .willReturn(booking);

        given(bookingMapper.toResponse(booking))
                .willReturn(response);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "items": [
                    {
                      "ticketCategoryId": 1,
                      "quantity": 3
                    }
                  ]
                }
                """))
                .andExpect(status().isOk());

        verify(bookingService).createBooking(any(CreateBookingRequest.class));
        verify(bookingMapper).toResponse(booking);
    }
    @Test
    void getBooking_success() throws Exception {

        Booking booking = new Booking();
        BookingResponse response = new BookingResponse();

        given(bookingService.getById(1L)).willReturn(booking);
        given(bookingMapper.toResponse(booking)).willReturn(response);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk());

        verify(bookingService).getById(1L);
        verify(bookingMapper).toResponse(booking);
    }

    @Test
    void getAll_success() throws Exception {

        Booking booking = new Booking();
        BookingResponse response = new BookingResponse();

        given(bookingService.getAll()).willReturn(List.of(booking));
        given(bookingMapper.toResponse(booking)).willReturn(response);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());

        verify(bookingService).getAll();
        verify(bookingMapper).toResponse(booking);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelBooking_success() throws Exception {

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isOk());

        verify(bookingService).cancelBooking(1L);
    }
}