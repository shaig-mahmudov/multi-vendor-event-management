package org.ironhack.project.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VendorService vendorService;


    @Test
    @WithMockUser(roles = "VENDOR")
    void createMyVendor_success() throws Exception {

        CreateVendorRequest request = new CreateVendorRequest();
        request.setName("Test Vendor");
        request.setDescription("Test description");
        request.setLogoUrl("logo.png");

        VendorResponse response = new VendorResponse();

        given(vendorService.createMyVendor(any(CreateVendorRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/vendors/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(vendorService).createMyVendor(any(CreateVendorRequest.class));
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void getMyVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.getMyVendor()).willReturn(response);

        mockMvc.perform(get("/api/vendors/me"))
                .andExpect(status().isOk());

        verify(vendorService).getMyVendor();
    }


    @Test
    @WithMockUser(roles = "VENDOR")
    void updateMyVendor_success() throws Exception {

        UpdateVendorRequest request = new UpdateVendorRequest();
        request.setName("Updated Vendor");

        VendorResponse response = new VendorResponse();

        given(vendorService.updateMyVendor(any(UpdateVendorRequest.class)))
                .willReturn(response);

        mockMvc.perform(patch("/api/vendors/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(vendorService).updateMyVendor(any(UpdateVendorRequest.class));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(vendorService.getById(1L)).willReturn(response);

        mockMvc.perform(get("/api/vendors/1"))
                .andExpect(status().isOk());

        verify(vendorService).getById(1L);
    }
}