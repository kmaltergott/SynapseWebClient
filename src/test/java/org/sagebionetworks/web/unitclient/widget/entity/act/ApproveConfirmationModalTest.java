package org.sagebionetworks.web.unitclient.widget.entity.act;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.widget.asynch.JobTrackingWidget;
import org.sagebionetworks.web.client.widget.entity.act.ApproveConfirmationModal;
import org.sagebionetworks.web.client.widget.entity.act.ApproveConfirmationModalView;
import org.sagebionetworks.web.client.widget.entity.act.UserBadgeList;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;

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
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		dialog = new ApproveConfirmationModal(mockView, mockSynAlert, mockSynapseClient, mockGlobalApplicationState, mockProgressWidget, mockUserBadgeList);
		when(mockGlobalApplicationState.getSynapseProperty(anyString())).thenReturn("syn7444807");
	}
	
}