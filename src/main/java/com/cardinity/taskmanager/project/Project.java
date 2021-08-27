package com.cardinity.taskmanager.project;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.lang.NonNull;
import com.cardinity.taskmanager.auth.Auditable;
import com.cardinity.taskmanager.task.Task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project extends Auditable<String>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@NonNull
	@Column(name="name",nullable = false)
    private String name;
	
	@OneToMany(mappedBy = "project",fetch = FetchType.LAZY, orphanRemoval = true)
	private Collection<Task> tasks;
	
	public Project(String name) {
		this.name = name;
	}
	
}
