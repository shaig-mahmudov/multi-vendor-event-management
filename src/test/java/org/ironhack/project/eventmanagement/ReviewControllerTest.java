package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.request.review.UpdateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;
import org.ironhack.project.eventmanagement.service.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;


    @Test
    void create_success() throws Exception {

        ReviewResponse response = new ReviewResponse();

        given(reviewService.createReview(any(Long.class), any(CreateReviewRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/events/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "rating": 5,
                          "comment": "Great event"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(reviewService).createReview(any(Long.class), any(CreateReviewRequest.class));
    }


    @Test
    void list_success() throws Exception {

        ReviewResponse response = new ReviewResponse();

        given(reviewService.getReviewsForEvent(any(Long.class), any(PageRequest.class)))
                .willReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/events/1/reviews"))
                .andExpect(status().isOk());

        verify(reviewService).getReviewsForEvent(any(Long.class), any());
    }


    @Test
    void average_success() throws Exception {

        given(reviewService.getAverageRatingForEvent(1L))
                .willReturn(4.5);

        mockMvc.perform(get("/api/events/1/reviews/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4.5));

        verify(reviewService).getAverageRatingForEvent(1L);
    }


    @Test
    void update_success() throws Exception {

        ReviewResponse response = new ReviewResponse();

        given(reviewService.updateReview(any(Long.class), any(UpdateReviewRequest.class)))
                .willReturn(response);

        mockMvc.perform(patch("/api/events/1/reviews/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "rating": 4,
                          "comment": "Updated comment"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(reviewService).updateReview(any(Long.class), any(UpdateReviewRequest.class));
    }


    @Test
    void delete_success() throws Exception {

        mockMvc.perform(delete("/api/events/1/reviews/10"))
                .andExpect(status().isOk());

        verify(reviewService).deleteReview(10L);
    }
}