package edu.tamu.sketch.backend.models;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SRL_Course {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String courseName;
	
	@Persistent
	private Set<Long> srl_assignmentIds;
	
	@Persistent
	private Set<Long> srl_userIds;
	
	public String getCourseName() { 
		return courseName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Long> getSrl_userIds() {
		return srl_userIds;
	}

	public void setSrl_userIds(Set<Long> srl_userIds) {
		this.srl_userIds = srl_userIds;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Set<Long> getSrl_assignmentIds() {
		return srl_assignmentIds;
	}

	public void setSrl_assignmentIds(Set<Long> srl_assignmentIds) {
		this.srl_assignmentIds = srl_assignmentIds;
	}
	
	public void addUser(Long userId){
		srl_userIds.add(userId);
	}
	
	public void addAssignment(Long assignmentId){
		srl_assignmentIds.add(assignmentId);
	}
	
}
