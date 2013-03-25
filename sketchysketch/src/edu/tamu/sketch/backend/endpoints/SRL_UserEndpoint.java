package edu.tamu.sketch.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Course;
import edu.tamu.sketch.backend.models.SRL_Submission;
import edu.tamu.sketch.backend.models.SRL_User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

@Api(name = "supersketchysketch",
	version="v4") 
public class SRL_UserEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name="users.list")
	public CollectionResponse<SRL_User> listSRL_User(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<SRL_User> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(SRL_User.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<SRL_User>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (SRL_User obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<SRL_User> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="users.get")
	public SRL_User getSRL_User(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_User srl_user = null;
		try {
			srl_user = mgr.getObjectById(SRL_User.class, id);
		} finally {
			mgr.close();
		}
		return srl_user;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_user the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="users.add")
	public SRL_User insertSRL_User(SRL_User srl_user) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if(srl_user.getId() != null){ //we dont want to user containsSRL_User if this is a new user
				if (containsSRL_User(srl_user)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_user);
		} finally {
			mgr.close();
		}
		return srl_user;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_user the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="users.update")
	public SRL_User updateSRL_User(SRL_User srl_user) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_User(srl_user)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_user);
		} finally {
			mgr.close(); 
		}
		return srl_user;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="users.delete")
	public SRL_User removeSRL_User(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_User srl_user = null;
		try {
			srl_user = mgr.getObjectById(SRL_User.class, id);
			mgr.deletePersistent(srl_user);
		} finally {
			mgr.close();
		}
		return srl_user;
	}
	
	/**
	 * This method gets the courses registered to the user with userId. It uses the HTTP GET method
	 * @param userId the primary key of the user
	 * @return a collection of SRL_Courses
	 */
	@ApiMethod(name="users.courses.list",
			path="srl_user/{userId}/srl_course/",
			httpMethod = HttpMethod.GET)
	public List<SRL_Course> listUserCourses(
			@Named("userId") Long userId){	
		PersistenceManager mgr = getPersistenceManager();
		List<SRL_Course> result = new ArrayList<SRL_Course>();
		try{
			SRL_User user = mgr.getObjectById(SRL_User.class, userId);
			for(Long id : user.getSrl_courseIds()){
				result.add(mgr.getObjectById(SRL_Course.class,id));
			}
		} finally {
			mgr.close();
		}
		return result;
	}

	
	/**
	 * This method adds a course to the specified user. 
	 * Additionally, it adds the user to the course's reference as well.
	 * Uses HTTP POST
	 * @param userId Primary Key of SRL_User to add course to
	 * @param srl_courseId Primary Key of SRL_Course to add user to
	 * @return the course the user was added
	 */
	@ApiMethod(name="users.courses.insert",
			path="srl_user/{userId}/srl_course",
			httpMethod = HttpMethod.POST)
	public SRL_Course insertCourse(
			@Named("userId") Long userId, 
			SRL_Course srl_course){
		PersistenceManager mgr = getPersistenceManager();
		SRL_Course course = null;
		Transaction tx = mgr.currentTransaction();
		try{
			tx.begin();
				course = mgr.getObjectById(SRL_Course.class, srl_course.getId());
				SRL_User user = mgr.getObjectById(SRL_User.class,userId );
				user.addCourse(srl_course.getId());
				course.addUser(userId);  
			tx.commit();
		} finally {
			if (tx.isActive()){
		        tx.rollback(); // Error occurred so rollback the PM transaction
			}
			mgr.close();
		}
		return course;
	}
	
	@ApiMethod(name="users.questions.sumbissions.insert",
			path="srl_user/{userId}/srl_question/{quesionId}/srl_submission",
			httpMethod = HttpMethod.POST)
	public SRL_Submission insertSubmission(
			@Named("userId") Long srl_userId,
			@Named("questionId") Long srl_questionId,
			SRL_Submission srl_submission){
		PersistenceManager mgr = getPersistenceManager();
		try{
			if(srl_submission.getId() != null){
				if(containsSRL_Submission(srl_submission)){
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_submission);
			srl_submission.setSrl_questionId(srl_questionId);
			srl_submission.setSrl_userId(srl_userId);
		} finally{
			mgr.close();
		}
		return srl_submission;
	}

	private boolean containsSRL_User(SRL_User srl_user) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(SRL_User.class, srl_user.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}
	
	private boolean containsSRL_Submission(SRL_Submission srl_submission){
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try{
			mgr.getObjectById(SRL_Submission.class, srl_submission.getId());
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
