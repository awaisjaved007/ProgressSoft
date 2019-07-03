package com.progsoft.assignment;

import com.progsoft.assignment.controller.DealController;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
public class DealControllerTest {

    MockMvc mockMvc;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(DealController.class).build();
    }

    public void fileUploadSuccessTest(){
      /*  mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadFile")
                .file(file)
                .param("fileType", "Seller")
                .andExpect(status().isOk());*/
    }
}
