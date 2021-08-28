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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.cardinity.taskmanager.exception.InvalidAccessException;
import com.cardinity.taskmanager.exception.NotFoundException;
import com.cardinity.taskmanager.exception.TaskAlreadyClosedException;
import com.cardinity.taskmanager.project.Project;
import com.cardinity.taskmanager.project.ProjectRepository;
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
	
	public void save(TaskDto taskdto) throws Exception {
				
		Project project = projectRepository.findById(taskdto.getProjectId())
				   .orElseThrow(() ->new NotFoundException("Project not found"));
		
		if(!contains(taskdto.getStatus().toUpperCase())){
			throw new NotFoundException("Staus not found, please use OPEN,IN_PROGRESS,CLOSED");
		}
		Task task = new Task(taskdto.getDescription(),Status.valueOf(taskdto.getStatus().toUpperCase()));
		try {
			if(Objects.nonNull(taskdto.getDueDate())) {
				task.setDueDate(new SimpleDateFormat("dd-MM-yyyy").parse(taskdto.getDueDate()));
			}
		} catch(ParseException e){
			throw new ValidationException("Invalid date format for due date");
		}
		if(Objects.nonNull(project)) {
			task.setProject(project);
		}		
		taskRepository.save(task);		
	}
	
	public TaskDto getTask(Long id,HttpServletRequest request) throws Exception{
		
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Task task = taskRepository.findById(id).orElseThrow(()->new Exception());
		
		if (request.isUserInRole("ADMIN") || task.getCreatedBy().equals(principal.getUsername())) {
			return convertToDto(task);
		} else {
			throw new InvalidAccessException("Invalid access");
		}
		
	}
	
	public void update(TaskDto taskdto,HttpServletRequest request) throws Exception{
		Task task = taskRepository.findById(taskdto.getId())
				.orElseThrow(() ->new NotFoundException("Task not found"));
		
		if(!contains(taskdto.getStatus().toUpperCase())){
			throw new NotFoundException("Staus not found use OPEN,IN_PROGRESS,CLOSED");
		}
		
		if(task.getStatus().equals(Status.CLOSED)) {
			throw new TaskAlreadyClosedException("Error! Task already colsed");
		}
		
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(request.isUserInRole("ADMIN") || principal.getUsername().equals(task.getCreatedBy())) {
			task.setDescription(taskdto.getDescription());
			task.setStatus(Status.valueOf(taskdto.getStatus().toUpperCase()));
			try {
				if(Objects.nonNull(taskdto.getDueDate())) {
					task.setDueDate(new SimpleDateFormat("dd-MM-yyyy").parse(taskdto.getDueDate()));//TODO check valid date
				}
			} catch(ParseException e){
				throw new ValidationException("Invalid date format for due date");
			}
			taskRepository.save(task);
		}else {
			throw new InvalidAccessException("Invalid access");
		}
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
            whereCondition.and(task.status.eq(Status.valueOf(dto.getStatus())));
        }
        if (Objects.nonNull(dto.getProjectId())) {
        	Optional<Project> project = projectRepository.findById(dto.getProjectId());
        	if(project.isPresent()) {
        		whereCondition.and(task.project.eq(project.get()));
        	}
        }
        //Additional search criteria for ADMIN
        if (request.isUserInRole("ADMIN")) {
        	if (Objects.nonNull(dto.getTaskByUser())) {
        		whereCondition.and(task.createdBy.eq(dto.getTaskByUser()));
        	}
        	
        	if (Objects.nonNull(dto.getProjectByUser())) {
        		whereCondition.and(task.project.createdBy.eq(dto.getProjectByUser()));
        	}
        	
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
