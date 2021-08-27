package com.cardinity.taskmanager.project;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.cardinity.taskmanager.exception.InvalidAccessException;
import com.cardinity.taskmanager.exception.NotFoundException;

@Service
@Transactional
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	
	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	
	public void save(ProjectDto dto){
				
		Project project = new Project(dto.getName());
		projectRepository.save(project);
		
	}
	
	public List<ProjectDto> findAll(HttpServletRequest request) {
		
		if (request.isUserInRole("ADMIN")) {
			return projectRepository.findAll().stream()
					.map(this::convertToDto).collect(Collectors.toList());
		}
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		return projectRepository.findByCreatedBy(principal.getUsername()).stream()
					.map(this::convertToDto).collect(Collectors.toList());
		
	}
	
	public void delete(Long id,HttpServletRequest request) throws Exception{
		
		Project project = projectRepository.findById(id)
										   .orElseThrow(()->new NotFoundException("Project not found"));		
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(request.isUserInRole("ADMIN") || principal.getUsername().equals(project.getCreatedBy())) {
			projectRepository.delete(project);
		}else {
			throw new InvalidAccessException("Invalid access");
		}	
				
	}
	
	private ProjectDto convertToDto(Project project) {
		return new ProjectDto(project.getName());
	}

}
