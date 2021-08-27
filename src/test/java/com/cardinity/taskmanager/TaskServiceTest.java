package com.cardinity.taskmanager;
/*
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import com.cardinity.taskmanager.project.Project;
import com.cardinity.taskmanager.project.ProjectRepository;
import com.cardinity.taskmanager.task.Status;
import com.cardinity.taskmanager.task.Task;
import com.cardinity.taskmanager.task.TaskDto;
import com.cardinity.taskmanager.task.TaskRepository;
import com.cardinity.taskmanager.task.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
	
	@InjectMocks
	private TaskService service;

	@Mock
	private TaskRepository repository;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	private ProjectRepository projectRepository;

	@Test
	@WithMockUser(username = "user", password = "user", roles = "USER")
	public void retrieveAllItems_basic() throws Exception{
		Task mockt = new Task("demo task",Status.OPEN);
		mockt.setId(1L);
		mockt.setProject(new Project("cardinity"));
		
		when(repository.findById(1L)).thenReturn(Optional.of(mockt));
		TaskDto items = service.getTask(1L, request);
		
		assertEquals("demo task", items.getDescription());
		//assertEquals(400, items.get(1).getValue());
	}

}*/
