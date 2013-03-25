package edu.tamu.sketch.backend.models;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SRL_Assignment {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long Id;
	
	@Persistent
	private Set<Long> srl_questionIds;
	
	@Persistent
	private Long courseId;
	
	@Persistent
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Set<Long> getSrl_questionIds() {
		return srl_questionIds;
	}

	public void setSrl_questionIds(Set<Long> srl_questionIds) {
		this.srl_questionIds = srl_questionIds;
	}
	
}
