package org.sagebionetworks.web.client.widget.entity.act;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sagebionetworks.repo.model.ACTAccessApproval;
import org.sagebionetworks.repo.model.ACTAccessRequirement;
import org.sagebionetworks.repo.model.ACTApprovalStatus;
import org.sagebionetworks.repo.model.AccessApproval;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.asynch.AsynchronousResponseBody;
import org.sagebionetworks.repo.model.table.Query;
import org.sagebionetworks.repo.model.table.QueryBundleRequest;
import org.sagebionetworks.repo.model.table.QueryResult;
import org.sagebionetworks.repo.model.table.QueryResultBundle;
import org.sagebionetworks.repo.model.table.Row;
import org.sagebionetworks.repo.model.table.RowSet;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.utils.GovernanceServiceHelper;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.widget.asynch.AsynchronousProgressHandler;
import org.sagebionetworks.web.client.widget.asynch.JobTrackingWidget;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.client.widget.table.v2.results.QueryBundleUtils;
import org.sagebionetworks.web.shared.asynch.AsynchType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ApproveConfirmationModal implements ApproveConfirmationModalView.Presenter, IsWidget {
	
	public static final String EMAIL_SUBJECT = "Data access approval";
	public static final String SELECT_FROM = "SELECT \"Email Body\" FROM ";
	public static final String WHERE = " WHERE \"Dataset Id\"= \"";	
	public static final String QUERY_CANCELLED = "Query cancelled";
	public static final String APPROVE_BUT_FAIL_TO_EMAIL = "User has been approved, but an error was encountered while emailing them: ";
	public static final String REJECT_BUT_FAIL_TO_EMAIL = "An error was encountered while emailing the users: ";
	public static final String APPROVED_USER = "Successfully Approved Request";
	public static final String REJECTED_USER = "Successfully Rejected Request";
	public static final String EMAIL_SENT = "An email has been sent to notify them";
	public static final String MESSAGE_BLANK = "You must enter a message in order to submit";
	
	// Mask to get all parts of a query.
	private static final Long ALL_PARTS_MASK = new Long(255);
	
	private Long accessRequirement;
	private String userId;
	private String datasetId;
	private String message;
	private EntityBundle entityBundle;
	private boolean approve;
	private Set<String> users;
	
	private ApproveConfirmationModalView view;
	private SynapseAlert synAlert;
	private SynapseClientAsync synapseClient;
	private GlobalApplicationState globalApplicationState;
	private JobTrackingWidget progressWidget;
	private UserBadgeList userBadgeList;
	
	@Inject
	public ApproveConfirmationModal(ApproveConfirmationModalView view,
			SynapseAlert synAlert,
			SynapseClientAsync synapseClient,
			GlobalApplicationState globalApplicationState,
			JobTrackingWidget progressWidget,
			UserBadgeList userBadgeList) {
		this.view = view;
		this.synAlert = synAlert;
		this.synapseClient = synapseClient;
		this.globalApplicationState = globalApplicationState;
		this.progressWidget = progressWidget;
		this.userBadgeList = userBadgeList;
		this.view.setUserListWidget(userBadgeList.asWidget());
		this.view.setPresenter(this);
	}

	public void configure(ACTAccessRequirement accessRequirement, List<String> users, EntityBundle bundle, boolean approve) {
		this.approve = approve;
		this.users = new HashSet<String>(users);
		view.setState(approve);
		this.userId = "3345921";
		this.entityBundle = bundle;
		this.accessRequirement = accessRequirement.getId();
		view.setAccessReqNumber(this.accessRequirement);
		view.setAccessRequirement(GovernanceServiceHelper.getAccessRequirementText(accessRequirement));
		view.setSynAlert(synAlert.asWidget());
		userBadgeList.configure(users, true, true);
		datasetId = entityBundle.getEntity().getId(); //get synId of dataset we are currently on
		if (approve) {
			view.setLoadingEmailWidget(this.progressWidget.asWidget());
			view.startLoadingEmail();
			loadEmailMessage();
		}
	}
	
	private void loadEmailMessage() {
		Query query = getDefaultQuery();
		QueryBundleRequest qbr = new QueryBundleRequest();
		qbr.setPartMask(ALL_PARTS_MASK);
		qbr.setQuery(query);
		qbr.setEntityId(QueryBundleUtils.getTableId(query));
		this.progressWidget.startAndTrackJob("Running query...", false, AsynchType.TableQuery, qbr, new AsynchronousProgressHandler() {
			
			@Override
			public void onFailure(Throwable failure) {
				view.setLoadingEmailVisible(false);
				synAlert.handleException(failure);
			}
			
			@Override
			public void onComplete(AsynchronousResponseBody response) {
				QueryResultBundle result = (QueryResultBundle) response;
				if (hasResult(result)) {
					message = result.getQueryResult().getQueryResults().getRows().get(0).getValues().get(0);
					view.setMessageEditArea(message);
				} else {
					message = "";
				}
				view.setMessageBody(message);
				view.finishLoadingEmail();
			}

			@Override
			public void onCancel() {
				view.finishLoadingEmail();
				synAlert.showError(QUERY_CANCELLED);
			}
		});
		view.setLoadingEmailWidget(progressWidget.asWidget());
	}
	
	private boolean hasResult(QueryResultBundle result) {
		QueryResult qr = result.getQueryResult();
		if (qr != null) {
			RowSet rs = qr.getQueryResults();
			if (rs != null) {
				List<Row> rowList = rs.getRows();
				if (rowList != null && rowList.size() > 0) {
					Row r = rowList.get(0);
					if (r != null) {
						List<String> strList = r.getValues();
						if (strList != null && strList.size() > 0) {
							return strList.get(0) != null;
						}
					}
				}
			}
		}
		return false;
	}
	
	public Query getDefaultQuery() {
		StringBuilder builder = new StringBuilder();
		builder.append(SELECT_FROM);
		builder.append(globalApplicationState.getSynapseProperty("org.sagebionetworks.portal.act.synapse_storage_id"));
		builder.append(WHERE);
		builder.append(datasetId + "\"");
		
		Query query = new Query();
		query.setSql(builder.toString());
		query.setIsConsistent(true);
		return query;
	}
	
	public void show() {
		synAlert.clear();
		view.show();
	}
	
	@Override
	public void onSubmit() {
		message = view.getEmailMessage();
		if (message == null || message.isEmpty()) {
			synAlert.showError(MESSAGE_BLANK);
			return;
		}
		view.setProcessing(true);
		if (approve) {
			approveRequest();
		} else {
			rejectRequest();
		}
	}
	
	private void approveRequest() {
		ACTAccessApproval aa  = new ACTAccessApproval();
		aa.setAccessorId(userId);  //user id - mine for now; eventually will be a list of users
		aa.setApprovalStatus(ACTApprovalStatus.APPROVED);
		aa.setRequirementId(accessRequirement);
		synapseClient.createAccessApproval(aa, new AsyncCallback<AccessApproval>() {

			@Override
			public void onFailure(Throwable caught) {
				synAlert.handleException(caught);
				view.setProcessing(false);
			}

			@Override
			public void onSuccess(AccessApproval result) {
				sendEmail();						
			}
		});
	}
	
	private void rejectRequest() {
		// need to update table containing requests to mark this request as rejected
		// don't remove access that might already exist
		sendEmail();
	}
	
	private void sendEmail() {
		synapseClient.sendMessage(users, EMAIL_SUBJECT, message, null, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				view.setProcessing(false);
				synAlert.showError((approve ? APPROVE_BUT_FAIL_TO_EMAIL : REJECT_BUT_FAIL_TO_EMAIL) + caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				view.setProcessing(false);
				view.hide();
				view.showInfo(approve ? APPROVED_USER : REJECTED_USER, EMAIL_SENT);
			}
		});
	}
	
	public Widget asWidget() {
		view.setPresenter(this);			
		return view.asWidget();
	}
		
}
