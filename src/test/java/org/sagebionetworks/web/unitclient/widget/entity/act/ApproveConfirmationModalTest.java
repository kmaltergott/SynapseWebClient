package org.sagebionetworks.web.unitclient.widget.entity.act;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.sagebionetworks.repo.model.ACTAccessRequirement;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.repo.model.asynch.AsynchronousRequestBody;
import org.sagebionetworks.repo.model.asynch.AsynchronousResponseBody;
import org.sagebionetworks.repo.model.table.Query;
import org.sagebionetworks.repo.model.table.QueryBundleRequest;
import org.sagebionetworks.repo.model.table.QueryResultBundle;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.utils.GovernanceServiceHelper;
import org.sagebionetworks.web.client.widget.asynch.AsynchronousProgressHandler;
import org.sagebionetworks.web.client.widget.asynch.JobTrackingWidget;
import org.sagebionetworks.web.client.widget.entity.act.ApproveConfirmationModal;
import org.sagebionetworks.web.client.widget.entity.act.ApproveConfirmationModalView;
import org.sagebionetworks.web.client.widget.entity.act.UserBadgeList;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.client.widget.table.v2.results.QueryBundleUtils;
import org.sagebionetworks.web.shared.asynch.AsynchType;

import static org.sagebionetworks.web.client.widget.entity.act.ApproveConfirmationModal.*;

public class ApproveConfirmationModalTest {


	ApproveConfirmationModal dialog;
	
	@Mock
	ApproveConfirmationModalView mockView;
	@Mock
	SynapseAlert mockSynAlert;
	@Mock
	SynapseClientAsync mockSynapseClient;
	@Mock
	GlobalApplicationState mockGlobalApplicationState;
	@Mock
	JobTrackingWidget mockProgressWidget;
	@Mock
	UserBadgeList mockUserBadgeList;
	
	@Mock
	ACTAccessRequirement mockAccReq;
	@Mock
	EntityBundle mockEntityBundle;
	@Mock
	Entity mockEntity;
	@Captor
	ArgumentCaptor<AsynchronousProgressHandler> aph;
	
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		dialog = new ApproveConfirmationModal(mockView, mockSynAlert, mockSynapseClient, mockGlobalApplicationState, mockProgressWidget, mockUserBadgeList);
		when(mockGlobalApplicationState.getSynapseProperty(anyString())).thenReturn("syn7444807");
		when(mockEntityBundle.getEntity()).thenReturn(mockEntity);
	}
	
	@Test
	public void testConfigureApprove() {
		dialog.configure(mockAccReq, new ArrayList<String>(), mockEntityBundle, true);
		verify(mockView).setState(true);
		verify(mockView, times(2)).setLoadingEmailWidget(mockProgressWidget.asWidget());
		verify(mockView).startLoadingEmail();
		verify(mockView).setAccessReqNumber(anyLong());
		verify(mockView).setAccessRequirement(anyString());
		verify(mockView).setSynAlert(mockSynAlert.asWidget());
	}
	
	@Test
	public void testConfigureReject() {
		dialog.configure(mockAccReq, new ArrayList<String>(), mockEntityBundle, false);
		verify(mockView).setState(false);
		verify(mockView, times(0)).setLoadingEmailWidget(mockProgressWidget.asWidget());
		verify(mockView, times(0)).startLoadingEmail();
		verify(mockView).setAccessReqNumber(anyLong());
		verify(mockView).setAccessRequirement(anyString());
		verify(mockView).setSynAlert(mockSynAlert.asWidget());
	}
	
	@Test
	public void testProgressWidgetFailure() {
		dialog.configure(mockAccReq, new ArrayList<String>(), mockEntityBundle, true);
		verify(mockProgressWidget).startAndTrackJob(anyString(), anyBoolean(), eq(AsynchType.TableQuery), any(AsynchronousRequestBody.class), aph.capture());
		aph.getValue().onFailure(any(Throwable.class));
		verify(mockView).setLoadingEmailVisible(false);
		verify(mockSynAlert).handleException(any(Throwable.class));
	}
	
	@Test
	public void testProgressWidgetCancel() {
		dialog.configure(mockAccReq, new ArrayList<String>(), mockEntityBundle, true);
		verify(mockProgressWidget).startAndTrackJob(anyString(), anyBoolean(), eq(AsynchType.TableQuery), any(AsynchronousRequestBody.class), aph.capture());
		aph.getValue().onCancel();
		verify(mockView).setLoadingEmailVisible(false);
		verify(mockView).finishLoadingEmail();
		verify(mockSynAlert).showError(QUERY_CANCELLED);
	}

	@Test
	public void testProgressWidgetComplete() {
		QueryResultBundle qrb = new QueryResultBundle();
		dialog.configure(mockAccReq, new ArrayList<String>(), mockEntityBundle, true);
		verify(mockProgressWidget).startAndTrackJob(anyString(), anyBoolean(), eq(AsynchType.TableQuery), any(AsynchronousRequestBody.class), aph.capture());
		aph.getValue().onComplete(qrb);
		verify(mockView).finishLoadingEmail();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}