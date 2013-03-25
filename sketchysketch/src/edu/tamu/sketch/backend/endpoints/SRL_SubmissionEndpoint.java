package edu.tamu.sketch.backend.endpoints;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Submission;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "supersketchysketch",
	version="v4")
public class SRL_SubmissionEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name="users.assignments.questions.submissions.list",
			path="srl_user/{userId}/srl_assignment/{assignmentId}/srl_question/{questionId}/srl_submission/")
	public CollectionResponse<SRL_Submission> listSRL_Submission(
			@Named("userId") Long srl_userId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("questionId") Long srl_questionId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<SRL_Submission> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(SRL_Submission.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<SRL_Submission>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (SRL_Submission obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<SRL_Submission> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="users.assignments.questions.submissions.get",
			path="srl_user/{userId}/srl_assignment/{assignmentId}/srl_question/{questionId}/srl_submission/{id}")
	public SRL_Submission getSRL_Submission(
			@Named("userId") Long srl_userId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("questionId") Long srl_questionId,
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Submission srl_submission = null;
		try {
			srl_submission = mgr.getObjectById(SRL_Submission.class, id);
		} finally {
			mgr.close();
		}
		return srl_submission;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_submission the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="users.assignments.questions.submissions.insert",
			path="srl_user/{userid}/srl_assignment/{assignmentId}/srl_question/{questionId}/srl_submission")
	public SRL_Submission insertSRL_Submission(
			@Named("userId") Long srl_userId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("questionId") Long srl_questionId,
			SRL_Submission srl_submission) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if(srl_submission.getId() != null){
				if (containsSRL_Submission(srl_submission)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_submission);
			srl_submission.setQuestonId(srl_questionId);
		} finally {
			mgr.close();
		}
		return srl_submission;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_submission the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="users.assignments.questions.submissions.update",
			path="srl_user/{userId}/srl_assignment/{assignmentId}/srl_question/{questionId}/srl_submission")
	public SRL_Submission updateSRL_Submission(
			@Named("userId") Long srl_userId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("questionId") Long srl_questionId,
			SRL_Submission srl_submission) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_Submission(srl_submission)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_submission);
		} finally {
			mgr.close();
		}
		return srl_submission;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="users.assignments.questions.submissions.delete",
			path="srl_user/{userId}/srl_assignment/{assignmentId}/srl_question/{questionId}/srl_submission/{id}")
	public SRL_Submission removeSRL_Submission(
			@Named("userId") Long srl_userId,
			@Named("assignmentId") Long srl_assignmentId,
			@Named("questionId") Long srl_questionId,
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Submission srl_submission = null;
		try {
			srl_submission = mgr.getObjectById(SRL_Submission.class, id);
			mgr.deletePersistent(srl_submission);
		} finally {
			mgr.close();
		}
		return srl_submission;
	}

	private boolean containsSRL_Submission(SRL_Submission srl_submission) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
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
