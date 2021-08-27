package com.cardinity.taskmanager.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import com.cardinity.taskmanager.auth.Auditable;
import com.cardinity.taskmanager.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Task extends Auditable<String>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@NonNull
	@Column(name="description",nullable = false)
    private String description;
	
	@Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
	
	@Column(name = "due_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	@ManyToOne
	private Project project;
	
	public Task(String description,Status status) {
		this.description =description;
		this.status = status;
	}

}
