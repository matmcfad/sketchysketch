package edu.tamu.sketch.backend.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SRL_Submission {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long Id;
	
	@Persistent
	private Long srl_questionId;
	
	@Persistent
	private Long srl_userId;
	
	@Persistent
	private int sketch;


	public Long getQuestonId() {
		return srl_questionId;
	}

	public void setQuestonId(Long srl_questionId) {
		this.srl_questionId = srl_questionId;
	}

	public int getSketch() {
		return sketch;
	}

	public void setSketch(int sketch) {
		this.sketch = sketch;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getSrl_questionId() {
		return srl_questionId;
	}

	public void setSrl_questionId(Long srl_questionId) {
		this.srl_questionId = srl_questionId;
	}

	public Long getSrl_userId() {
		return srl_userId;
	}

	public void setSrl_userId(Long srl_userId) {
		this.srl_userId = srl_userId;
	}
}
