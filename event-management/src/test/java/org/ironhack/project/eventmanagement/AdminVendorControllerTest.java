package org.ironhack.project.eventmanagement;

import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.VendorStatus;
import org.ironhack.project.eventmanagement.service.admin.AdminVendorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminVendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminVendorService adminVendorService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void approveVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(adminVendorService.updateVendorStatus(1L, VendorStatus.APPROVED))
                .willReturn(response);

        mockMvc.perform(patch("/api/admin/vendors/1/approve"))
                .andExpect(status().isOk());

        verify(adminVendorService).updateVendorStatus(1L, VendorStatus.APPROVED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void rejectVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(adminVendorService.updateVendorStatus(1L, VendorStatus.REJECTED))
                .willReturn(response);

        mockMvc.perform(patch("/api/admin/vendors/1/reject"))
                .andExpect(status().isOk());

        verify(adminVendorService).updateVendorStatus(1L, VendorStatus.REJECTED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void suspendVendor_success() throws Exception {

        VendorResponse response = new VendorResponse();

        given(adminVendorService.updateVendorStatus(1L, VendorStatus.SUSPENDED))
                .willReturn(response);

        mockMvc.perform(patch("/api/admin/vendors/1/suspend"))
                .andExpect(status().isOk());

        verify(adminVendorService).updateVendorStatus(1L, VendorStatus.SUSPENDED);
    }
}