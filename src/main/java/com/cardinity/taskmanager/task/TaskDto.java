package com.cardinity.taskmanager.task;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class TaskDto {
	
	private Long id;
	
	@NotBlank(message = "Error! Please provide valid task status")
	private String status;
	
	@NotEmpty(message = "Error! Please provide valid description")
	private String description;
	
	@Positive(message = "Error! Please provide valid project id")
	private Long projectId;
	
	private String dueDate;
	
	//private String username;

}
