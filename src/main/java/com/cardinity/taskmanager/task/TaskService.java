package com.cardinity.taskmanager.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.cardinity.taskmanager.exception.InvalidAccessException;
import com.cardinity.taskmanager.exception.NotFoundException;
import com.cardinity.taskmanager.exception.TaskAlreadyClosedException;
import com.cardinity.taskmanager.project.Project;
import com.cardinity.taskmanager.project.ProjectRepository;
import com.cardinity.taskmanager.utill.Constant;
import com.cardinity.taskmanager.utill.Permission;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Service
@Transactional
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final EntityManager entityManager;
	
	public TaskService(TaskRepository taskRepository,
						ProjectRepository projectRepository,
						EntityManager entityManager) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.entityManager = entityManager;
	}
	
	public void save(TaskDto taskdto,HttpServletRequest request) throws Exception {
				
		Project project = projectRepository.findById(taskdto.getProjectId())
				   .orElseThrow(() ->new NotFoundException(Constant.PROJECT_NOT_FOUND));
		
		if(!Permission.hasAccess(request, project)) {
			throw new InvalidAccessException(Constant.INVALID_ACCESS);
		}
		
		if(!contains(taskdto.getStatus().toUpperCase())){
			throw new NotFoundException(Constant.INVALID_STATUS);
		}
		Task task = new Task(taskdto.getDescription(),Status.valueOf(taskdto.getStatus().toUpperCase()));
		try {
			if(Objects.nonNull(taskdto.getDueDate())) {
				task.setDueDate(new SimpleDateFormat("dd-MM-yyyy").parse(taskdto.getDueDate()));
			}
		} catch(ParseException e){
			throw new ValidationException(Constant.INVALID_FORMAT);
		}
		if(Objects.nonNull(project)) {
			task.setProject(project);
		}		
		taskRepository.save(task);		
	}
	
	public TaskDto getTask(Long id,HttpServletRequest request) throws Exception{
		
		Task task = taskRepository.findById(id).orElseThrow(()->new NotFoundException(Constant.TASK_NOT_FOUND));		
		if (!Permission.hasAccess(request,task)) {
			throw new InvalidAccessException(Constant.INVALID_ACCESS);
		}
			
		return convertToDto(task);
		
	}
	
	public void update(TaskDto taskdto,HttpServletRequest request) throws Exception{
		
		Task task = taskRepository.findById(taskdto.getId())
				.orElseThrow(() ->new NotFoundException(Constant.TASK_NOT_FOUND));
		
		if(!Permission.hasAccess(request, task)) {
			throw new InvalidAccessException(Constant.INVALID_ACCESS);
		}
		
		Optional<Project> project = projectRepository.findById(taskdto.getProjectId());
		
		if(project.isPresent() && !Permission.hasAccess(request, project.get())) {
			throw new InvalidAccessException(Constant.INVALID_ACCESS);
		}
		
		if(!contains(taskdto.getStatus().toUpperCase())){
			throw new NotFoundException(Constant.INVALID_STATUS);
		}
		
		if(task.getStatus().equals(Status.CLOSED)) {
			throw new TaskAlreadyClosedException(Constant.TASK_CLOSED);
		}		

		task.setDescription(taskdto.getDescription());
		task.setStatus(Status.valueOf(taskdto.getStatus().toUpperCase()));
		if(Objects.nonNull(project)) {
			task.setProject(project.get());
		}
		
		try {
			if(Objects.nonNull(taskdto.getDueDate())) {
				task.setDueDate(new SimpleDateFormat("dd-MM-yyyy").parse(taskdto.getDueDate()));
			}
		} catch(ParseException e){
			throw new ValidationException(Constant.INVALID_FORMAT);
		}
		
		taskRepository.save(task);
		
	}
	
	public List<TaskDto> search(SearchCritera dto,HttpServletRequest request) throws ParseException{
		
		final JPAQuery<Task> query = new JPAQuery<>(entityManager);
        final QTask task = QTask.task;
        BooleanBuilder whereCondition = new BooleanBuilder();
        if (Objects.nonNull(dto.getIsExpire()) && dto.getIsExpire()) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date today = sdf.parse(sdf.format(new Date()));
            whereCondition.and(task.dueDate.before(today));
        }
        if (Objects.nonNull(dto.getStatus())) {
            whereCondition.and(task.status.eq(Status.valueOf(dto.getStatus().toUpperCase())));
        }
        if (Objects.nonNull(dto.getProjectId())) {
        	Optional<Project> project = projectRepository.findById(dto.getProjectId());
        	if(project.isPresent()) {
        		whereCondition.and(task.project.eq(project.get()));
        	}
        }
        
        if (request.isUserInRole("ADMIN")) {
        	if (Objects.nonNull(dto.getTaskByUser())) {
        		whereCondition.and(task.createdBy.eq(dto.getTaskByUser()));
        	}
        	
        	if (Objects.nonNull(dto.getProjectByUser())) {
        		whereCondition.and(task.project.createdBy.eq(dto.getProjectByUser()));
        	}
        	
		} else {
			whereCondition.and(task.createdBy.eq(Permission.currentUser()));
		}
        
        return query.from(task)
                .where(whereCondition)
                .fetch().stream()
                .map(reg -> new ModelMapper().map(reg, TaskDto.class))
                .collect(Collectors.toList());
	}
	
	private TaskDto convertToDto(Task task) {
		TaskDto postDto = new ModelMapper().map(task, TaskDto.class);
		if(Objects.nonNull(task.getDueDate())) {
			postDto.setDueDate(new SimpleDateFormat("dd-MM-yyyy").format(task.getDueDate()));
		}
	    return postDto;
	}
	
	public boolean contains(String item) {

	    for (Status c : Status.values()) {
	        if (c.name().equalsIgnoreCase(item.toUpperCase())) {
	            return true;
	        }
	    }

	    return false;
	}

}
