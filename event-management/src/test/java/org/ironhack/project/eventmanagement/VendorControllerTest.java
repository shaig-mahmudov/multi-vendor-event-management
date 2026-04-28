package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.request.vendor.CreateVendorRequest;
import org.ironhack.project.eventmanagement.dto.request.vendor.UpdateVendorRequest;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.service.vendor.VendorService;
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
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorService;


    @Test
    void createMyVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.createMyVendor(any(CreateVendorRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/vendors/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Tech Events",
                          "description": "Event organizer",
                          "logoUrl": "logo.png"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(vendorService).createMyVendor(any(CreateVendorRequest.class));
    }


    @Test
    void getMyVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.getMyVendor()).willReturn(response);

        mockMvc.perform(get("/api/vendors/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(vendorService).getMyVendor();
    }


    @Test
    void updateMyVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.updateMyVendor(any(UpdateVendorRequest.class)))
                .willReturn(response);

        mockMvc.perform(patch("/api/vendors/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Updated Vendor",
                          "description": "Updated desc"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(vendorService).updateMyVendor(any(UpdateVendorRequest.class));
    }


    @Test
    void getById_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.getById(1L)).willReturn(response);

        mockMvc.perform(get("/api/vendors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(vendorService).getById(1L);
    }
}