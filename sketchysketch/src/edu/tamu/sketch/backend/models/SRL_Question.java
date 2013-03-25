package edu.tamu.sketch.backend.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SRL_Question {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long Id;
	
	@Persistent
	private String question;
	
	@Persistent
	private Long srl_assignmentId;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getSrl_assignmentId() {
		return srl_assignmentId;
	}

	public void setSrl_assignmentId(Long srl_assignmentId) {
		this.srl_assignmentId = srl_assignmentId;
	}
}
