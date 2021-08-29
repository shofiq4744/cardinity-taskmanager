package com.cardinity.taskmanager.project;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.cardinity.taskmanager.exception.InvalidAccessException;
import com.cardinity.taskmanager.exception.NotFoundException;
import com.cardinity.taskmanager.utill.Constant;
import com.cardinity.taskmanager.utill.Permission;

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
		
		return projectRepository.findByCreatedBy(Permission.currentUser()).stream()
					.map(this::convertToDto).collect(Collectors.toList());
		
	}
	
	public void delete(Long id,HttpServletRequest request) throws Exception{
		
		Project project = projectRepository.findById(id)
										   .orElseThrow(()->new NotFoundException(Constant.PROJECT_NOT_FOUND));		
		
		if(!Permission.hasAccess(request, project)) {
			throw new InvalidAccessException(Constant.INVALID_ACCESS);
		}
		
		projectRepository.delete(project);		
				
	}
	
	private ProjectDto convertToDto(Project project) {
		
		return new ProjectDto(project.getName());
		
	}

}
