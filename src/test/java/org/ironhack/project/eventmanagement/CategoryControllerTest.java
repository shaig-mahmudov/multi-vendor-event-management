package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.request.category.CategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.CategoryResponse;
import org.ironhack.project.eventmanagement.service.category.CategoryService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;


    private CategoryResponse mockCategory() {
        return new CategoryResponse(
                1L,
                "Music",
                "Music events",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void create_success() throws Exception {

        given(categoryService.create(any(CategoryRequest.class)))
                .willReturn(mockCategory());

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Music",
                          "description": "Music events"
                        }
                        """))
                .andExpect(status().isCreated());

        verify(categoryService).create(any(CategoryRequest.class));
    }


    @Test
    void getAll_success() throws Exception {

        given(categoryService.getAll())
                .willReturn(List.of(mockCategory()));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(categoryService).getAll();
    }


    @Test
    void getById_success() throws Exception {

        given(categoryService.getById(1L))
                .willReturn(mockCategory());

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk());

        verify(categoryService).getById(1L);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void update_success() throws Exception {

        given(categoryService.update(any(Long.class), any(CategoryRequest.class)))
                .willReturn(mockCategory());

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Updated Music",
                          "description": "Updated desc"
                        }
                        """))
                .andExpect(status().isOk());

        verify(categoryService).update(any(Long.class), any(CategoryRequest.class));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_success() throws Exception {

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
    }
}