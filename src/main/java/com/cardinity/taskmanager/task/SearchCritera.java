package com.cardinity.taskmanager.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCritera {
	
	private String projectName;
	
	private Long projectId;
	
	private Boolean isExpire;
	
	private String status;
	
	private String userTask;
	
	private String userProject;

}
