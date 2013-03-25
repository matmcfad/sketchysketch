package edu.tamu.sketch.backend.endpoints;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Assignment;
import edu.tamu.sketch.backend.models.SRL_Question;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

@Api(name = "supersketchysketch",
	version="v4")
public class SRL_QuestionEndpoint {

	/**
	 * This method lists all the questions in this assignment
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A Collection class containing the list of all entities 
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings("unchecked")
	@ApiMethod(name="courses.assignments.questions.list",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}/srl_question/")
	public Collection<SRL_Question> listSRL_Question(
			@Named("courseId") Long srl_courseId,
			@Named("assignmentId")Long srl_assignmentId) {

		PersistenceManager mgr = getPersistenceManager();
		Collection<SRL_Question> result = new ArrayList<SRL_Question>();
		try{
			Query query = mgr.newQuery(SRL_Question.class);
			query.setFilter("srl_assignmentId == assignmentParam");
			query.declareParameters("Long assignmentParam");
			result = (Collection<SRL_Question>) query.execute(srl_assignmentId);
			
			/*
			SRL_Assignment assignment = mgr.getObjectById(SRL_Assignment.class, srl_assignmentId);
			for(Long id : assignment.getSrl_questionIds()){
				result.add(mgr.getObjectById(SRL_Question.class,id));
			}
			*/
		} finally {
			
			mgr.close();
		}
		return result;
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="courses.assignments.questions.get",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}/srl_question/{id}")
	public SRL_Question getSRL_Question(
			@Named("courseId") Long srl_courseId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Question srl_question = null;
		try {
			srl_question = mgr.getObjectById(SRL_Question.class, id);
		} finally {
			mgr.close();
		}
		return srl_question;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_question the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="courses.assignments.questions.insert",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}/srl_question")
	public SRL_Question insertSRL_Question(SRL_Question srl_question,
			@Named("courseId") Long srl_courseId,
			@Named("assignmentId") Long srl_assignmentId) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if(srl_question.getId() != null){
				if (containsSRL_Question(srl_question)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_question);
			srl_question.setSrl_assignmentId(srl_assignmentId);
		} finally {
			mgr.close();
		}
		return srl_question;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_question the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="courses.assignments.questions.update",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}/srl_question")
	public SRL_Question updateSRL_Question(
			@Named("courseId") Long srl_courseId,
			@Named("assignmentId")Long srl_assignmentId,
			SRL_Question srl_question) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_Question(srl_question)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_question);
		} finally {
			mgr.close();
		}
		return srl_question;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="courses.assignments.questions.delete",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}/srl_question/{id}")
	public SRL_Question removeSRL_Question(@Named("id") Long id,
			@Named("courseId") Long srl_courseId,
			@Named("assignmentId") Long srl_assignmentId) {
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		SRL_Question srl_question = null;
		try {
			tx.begin();
				srl_question = mgr.getObjectById(SRL_Question.class, id);
				mgr.deletePersistent(srl_question);
				SRL_Assignment srl_assignment = mgr.getObjectById(SRL_Assignment.class, srl_assignmentId);
				srl_assignment.getSrl_questionIds().remove(id);
			tx.commit();
		} finally {
			if (tx.isActive())
		    {
		        tx.rollback(); // Error occurred so rollback the PM transaction
		    }
			mgr.close();
		}
		return srl_question;
	}

	private boolean containsSRL_Question(SRL_Question srl_question) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(SRL_Question.class, srl_question.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
