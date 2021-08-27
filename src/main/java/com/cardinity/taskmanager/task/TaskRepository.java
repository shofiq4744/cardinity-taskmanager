package com.cardinity.taskmanager.task;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.cardinity.taskmanager.project.Project;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,QuerydslPredicateExecutor<Task>, QuerydslBinderCustomizer<QTask> {
	
	List<Task> findByCreatedBy(String username);
	
	List<Task> findByDueDateBefore(Date date);
	
	List<Task> findByStatus(Status status);
	
	List<Task> findByProject(Project project);
	
	@Override
    default void customize(QuerydslBindings bindings, QTask root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

}
