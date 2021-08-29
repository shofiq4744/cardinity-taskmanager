package com.cardinity.taskmanager.utill;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.cardinity.taskmanager.project.Project;
import com.cardinity.taskmanager.task.Task;

public class Permission {
	
	public static Boolean hasAccess(HttpServletRequest request,Task task) {
		
		if(request.isUserInRole("ADMIN")) return true;
		
		if(task.getCreatedBy().equals(currentUser())) {
			return true;
		}
		
		return false;
		
	}
	
	public static Boolean hasAccess(HttpServletRequest request,Project project) {
		
		if(request.isUserInRole("ADMIN")) return true;
		
		if(project.getCreatedBy().equals(currentUser())) {
			return true;
		}
		
		return false;
		
	}
	
	public static String currentUser() {
		
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		return principal.getUsername();
		
	}

}
