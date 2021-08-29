package com.cardinity.taskmanager.task;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cardinity.taskmanager.common.ApiController;
import com.cardinity.taskmanager.common.RestApiResponse;

@ApiController
public class TaskController {
	
	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService = taskService; 	
	}
	

	@PostMapping("/task")
	public RestApiResponse create(@Valid @RequestBody TaskDto task,HttpServletRequest request) {
		
		try {
			taskService.save(task,request);
			return RestApiResponse.SUCCESS;
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}

	@GetMapping("/task/{id}")
	public RestApiResponse findById(@PathVariable Long id,HttpServletRequest request) {
		
		try {
			return RestApiResponse.OK
								.setResponse(taskService.getTask(id,request));
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}
	
	@PutMapping("/task")
	public RestApiResponse update(@Valid @RequestBody TaskDto task,HttpServletRequest request) {
		
		try {
			taskService.update(task,request);
			return RestApiResponse.SUCCESS;
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}
	
	@PostMapping("/search")
	public RestApiResponse search(@RequestBody SearchCritera critaria,HttpServletRequest request) {
		
		try {			
			return RestApiResponse.OK
					.setResponse(taskService.search(critaria,request));
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}

}
