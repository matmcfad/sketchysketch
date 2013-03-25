package edu.tamu.sketch.backend.endpoints;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Course;
import edu.tamu.sketch.backend.models.SRL_User;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

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
public class SRL_CourseEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name="courses.list")
	public CollectionResponse<SRL_Course> listSRL_Course(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<SRL_Course> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(SRL_Course.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<SRL_Course>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (SRL_Course obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<SRL_Course> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="courses.get")
	public SRL_Course getSRL_Course(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Course srl_course = null;
		try {
			srl_course = mgr.getObjectById(SRL_Course.class, id);
		} finally {
			mgr.close();
		}
		return srl_course;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_course the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="courses.insert")
	public SRL_Course insertSRL_Course(SRL_Course srl_course) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if(srl_course.getId() != null){
				if (containsSRL_Course(srl_course)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_course);
		} finally {
			mgr.close();
		}
		return srl_course;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_course the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="courses.update")
	public SRL_Course updateSRL_Course(SRL_Course srl_course) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_Course(srl_course)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_course);
		} finally {
			mgr.close();
		}
		return srl_course;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="courses.delete")
	public SRL_Course removeSRL_Course(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Course srl_course = null;
		try {
			srl_course = mgr.getObjectById(SRL_Course.class, id);
			mgr.deletePersistent(srl_course);
		} finally {
			mgr.close();
		}
		return srl_course;
	}
	
	/**
	 * This method gets the students registered to a course. 
	 * It uses the HTTP GET method 
	 * @param courseId the primary key of the coruse
	 * @return a collection of SRL_Users
	 */
	@ApiMethod(name="courses.users.list",
			path="srl_course/{courseId}/users",
			httpMethod = HttpMethod.GET)
	public List<SRL_User> listSRL_CourseSRL_Users(
			@Named("courseId") Long courseId){
		PersistenceManager mgr = getPersistenceManager();
		List<SRL_User> result = new ArrayList<SRL_User>();
		try{
			SRL_Course course = mgr.getObjectById(SRL_Course.class, courseId);
			for(Long id : course.getSrl_userIds()){
				result.add(mgr.getObjectById(SRL_User.class, id));
			}
		} finally {
			mgr.close();
		}
		return result;
	}
	
	/**
	 * This method adds a user to the specified course. 
	 * Additionally, it adds the course to the user's reference as well.
	 * Uses HTTP POST
	 * @param courseId Primary Key of SRL_Course to add user to
	 * @param srl_userId Primary Key of SRL_User to add course to
	 * @return the user the course added
	 */
	@ApiMethod(name="courses.users.insert",
			path="srl_course/{courseId}/users",
			httpMethod = HttpMethod.POST)
	public SRL_User insertSRL_User(@Named("courseId") Long courseId, SRL_User srl_user){
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		SRL_User user = null;
		try{
			tx.begin();
				user = mgr.getObjectById(SRL_User.class, srl_user.getId());
				SRL_Course course = mgr.getObjectById(SRL_Course.class, courseId );
				course.addUser(user.getId());
				user.addCourse(course.getId());
			tx.commit();
		} finally {
			if (tx.isActive())
		    {
		        tx.rollback(); // Error occurred so rollback the PM transaction
		    }
			mgr.close();
		}
		return srl_user;
	}
	
	

	private boolean containsSRL_Course(SRL_Course srl_course) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(SRL_Course.class, srl_course.getId());
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
