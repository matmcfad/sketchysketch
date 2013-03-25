package edu.tamu.sketch.backend.endpoints;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Assignment;
import edu.tamu.sketch.backend.models.SRL_Course;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

@Api(name = "supersketchysketch",
	version="v4")
public class SRL_AssignmentEndpoint {

	/**
	 * This method lists all the assignments assigned by the course.
	 * It uses HTTP GET
	 * @param srl_courseId
	 * @return
	 */
	@ApiMethod(name="courses.assignments.list",
			path="srl_course/{courseId}/srl_assignment",
			httpMethod = HttpMethod.GET)
	public Collection<SRL_Assignment> listSRL_Assignments(
			@Named("courseId") Long srl_courseId){
		PersistenceManager mgr = getPersistenceManager();
		Collection<SRL_Assignment> result = new ArrayList<SRL_Assignment>();
		try{
			SRL_Course course = mgr.getObjectById(SRL_Course.class, srl_courseId);
			for(Long id : course.getSrl_assignmentIds()){
				result.add(mgr.getObjectById(SRL_Assignment.class,id));
			}
		} finally {
			
			mgr.close();
		}
		return result;
	}
	
	/**
	 * This method gets an assignment from a course
	 * @param courseId
	 * @param assignmentId
	 * @return
	 */
	@ApiMethod(name="courses.assignments.get",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}",
			httpMethod = HttpMethod.GET)
	public SRL_Assignment getSRL_Assignment(
			@Named("courseId") Long courseId,
			@Named("assignmentId") Long assignmentId){
		PersistenceManager mgr = getPersistenceManager();
		SRL_Assignment srl_assignment = null;
		try{
			mgr.getObjectById(SRL_Course.class, courseId);
			srl_assignment = mgr.getObjectById(SRL_Assignment.class, assignmentId);
		}finally{
			mgr.close();
		}
		return srl_assignment;
	}
	
	/**
	 * This inserts a new entity, an assignment, into App Engine datastore as a child of this assignment. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_user the assignment to be inserted.
	 * @return The inserted assignment.
	 */
	@ApiMethod(name="courses.assignments.add",
			path="srl_course/{courseId}/srl_assignment",
			httpMethod = HttpMethod.POST)
	public SRL_Assignment insertSRL_Assignment(
			@Named("courseId") Long courseId,
			SRL_Assignment srl_assignment){
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		try{
			if(srl_assignment.getId() != null){
				if(containsSRL_Assignment(srl_assignment)){
					throw new EntityExistsException("Assignment already exists");
				}
			}
			tx.begin();
				SRL_Course course = mgr.getObjectById(SRL_Course.class, courseId);
				mgr.makePersistent(srl_assignment);
				course.addAssignment(srl_assignment.getId());
			tx.commit();
		} finally {
			if (tx.isActive())
		    {
		        tx.rollback(); // Error occurred so rollback the PM transaction
		    }
			mgr.close();
		}
		
		return srl_assignment;
	}
	
	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_user the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="courses.assigments.update",
			path="srl_course/{courseId}/srl_assignment",
			httpMethod = HttpMethod.PUT)
	public SRL_Assignment updateSRL_Assignment(
			@Named("courseId") Long srl_courseId,
			SRL_Assignment srl_assignment) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_Assignment(srl_assignment)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_assignment);
		} finally {
			mgr.close();
		}
		return srl_assignment;
	}
	
	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="courses.assignments.delete",
			path="srl_course/{courseId}/srl_assignment/{assignmentId}",
			httpMethod = HttpMethod.DELETE)
	public SRL_Assignment removeSRL_Assignment(
			@Named("courseId") Long courseId,
			@Named("assignmentId") Long assignmentId) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Assignment srl_assignment = null;
		SRL_Course srl_course = null;
		Transaction tx = mgr.currentTransaction();
		try {
			tx.begin();
				srl_course = mgr.getObjectById(SRL_Course.class,courseId);
				srl_assignment = mgr.getObjectById(SRL_Assignment.class, assignmentId);
				srl_course.getSrl_assignmentIds().remove(assignmentId);
				mgr.deletePersistent(srl_assignment);
			tx.commit();
		} finally {
			if (tx.isActive())
		    {
		        tx.rollback(); // Error occurred so rollback the PM transaction
		    }
			mgr.close();
		}
		return srl_assignment;
	}
	
	private boolean containsSRL_Assignment(SRL_Assignment srl_assignment) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(SRL_Assignment.class, srl_assignment.getId());
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
