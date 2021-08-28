package com.cardinity.taskmanager;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cardinity.taskmanager.project.ProjectRepository;
import com.cardinity.taskmanager.project.ProjectService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Mock
	private ProjectService service;

	@Mock
	private ProjectRepository repository;

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void givenGetAllProject_thenOk() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
   

}
