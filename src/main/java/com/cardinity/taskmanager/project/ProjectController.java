package com.cardinity.taskmanager.project;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cardinity.taskmanager.common.ApiController;
import com.cardinity.taskmanager.common.RestApiResponse;

@ApiController
public class ProjectController {
	
	private final ProjectService projectService;
	
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService; 	
	}
	
	@PostMapping("/project")
	public RestApiResponse create(@Valid @RequestBody ProjectDto dto) {
		
		try {
			projectService.save(dto);
			return RestApiResponse.SUCCESS;
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}
	
	@GetMapping("/projects")
	public RestApiResponse findAll(HttpServletRequest request) {
		
		try {
			return RestApiResponse.OK
					.setResponse(projectService.findAll(request));
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}
	
	@DeleteMapping("/project/{id}")
	public RestApiResponse delete(@PathVariable Long id,HttpServletRequest request) {
		
		try {
			projectService.delete(id,request);
			return RestApiResponse.SUCCESS;
		} catch(Exception e) {
			return RestApiResponse.ERROR.setResponse(null).setMessage(e.getMessage());
		}
		
	}

}
