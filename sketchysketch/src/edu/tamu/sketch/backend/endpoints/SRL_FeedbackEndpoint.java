package edu.tamu.sketch.backend.endpoints;

import edu.tamu.sketch.backend.models.PMF;
import edu.tamu.sketch.backend.models.SRL_Feedback;

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
public class SRL_FeedbackEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name="users.submissions.feedback.list",
			path="srl_user/{userId}/srl_submission/{submissionId}/srl_feedback")
	public CollectionResponse<SRL_Feedback> listSRL_Feedback(
			@Named("userId") Long srl_userId,
			@Named("submissionId") Long srl_submissionId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<SRL_Feedback> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(SRL_Feedback.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<SRL_Feedback>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (SRL_Feedback obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<SRL_Feedback> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="users.submissions.feedback.get",
			path="srl_user/{userId}/srl_submission/{submissionId}/srl_feedback/{id}")
	public SRL_Feedback getSRL_Feedback(
			@Named("userId") Long userId,
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Feedback srl_feedback = null;
		try {
			srl_feedback = mgr.getObjectById(SRL_Feedback.class, id);
		} finally {
			mgr.close();
		}
		return srl_feedback;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param srl_feedback the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="users.submissions.feedback.insert",
			path="srl_user/{userId}/srl_submission/{submissionId}/srl_feedback")
	public SRL_Feedback insertSRL_Feedback(
			@Named("userId") Long srl_userId,
			@Named("submissionId") Long srl_submissionId,
			SRL_Feedback srl_feedback) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if(srl_feedback.getId() != null){
				if (containsSRL_Feedback(srl_feedback)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(srl_feedback);
			srl_feedback.setSrl_submissionId(srl_submissionId);
		} finally {
			mgr.close();
		}
		return srl_feedback;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param srl_feedback the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name="users.submissions.feedback.update",
			path="srl_user/{userId}/srl_submission/{submissionId}/srl_feedback")
	public SRL_Feedback updateSRL_Feedback(
			@Named("userId") Long srl_userId,
			@Named("submissionId") Long srl_submissionId,
			SRL_Feedback srl_feedback) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSRL_Feedback(srl_feedback)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(srl_feedback);
		} finally {
			mgr.close();
		}
		return srl_feedback;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name="users.submissions.feedback.remove",
			path="srl_user/{userId}/srl_submission/{submissionId}/srl_feedback")
	public SRL_Feedback removeSRL_Feedback(
			@Named("userId") Long srl_userId,
			@Named("submissionId") Long srl_submissionId,
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		SRL_Feedback srl_feedback = null;
		try {
			srl_feedback = mgr.getObjectById(SRL_Feedback.class, id);
			mgr.deletePersistent(srl_feedback);
			
		} finally {
			mgr.close();
		}
		return srl_feedback;
	}

	private boolean containsSRL_Feedback(SRL_Feedback srl_feedback) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(SRL_Feedback.class, srl_feedback.getId());
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
