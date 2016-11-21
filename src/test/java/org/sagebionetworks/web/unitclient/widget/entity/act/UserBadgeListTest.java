package org.sagebionetworks.web.unitclient.widget.entity.act;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;


import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.widget.entity.act.UserBadgeItem;
import org.sagebionetworks.web.client.widget.entity.act.UserBadgeList;
import org.sagebionetworks.web.client.widget.entity.act.UserBadgeListView;

import com.google.gwt.user.client.ui.Widget;


public class UserBadgeListTest {

	UserBadgeList list;
	
	@Mock
	UserBadgeListView mockView;
	@Mock
	PortalGinInjector mockGinInjector;
	
	String userId;
	String userId2;
	
	@Mock
	UserBadgeItem mockUserBadgeItem;
	@Mock
	UserBadgeItem mockUserBadgeItem2;

	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		list = new UserBadgeList(mockView, mockGinInjector);
		userId = "1234567";
		userId2 = "9876543";
		when(mockGinInjector.getUserBadgeItem()).thenReturn(mockUserBadgeItem);
		when(mockUserBadgeItem.getUserId()).thenReturn(userId);
		when(mockUserBadgeItem2.getUserId()).thenReturn(userId2);

		list.configure(true, true);
	}
	
	@Test
	public void testConfigure() {
		verify(mockView).setToolbarVisible(false);
	}	
	
	@Test
	public void testConfigureList() {
		List<String> users = new ArrayList<String>();
		users.add(userId);
		users.add(userId2);
		list.configure(users, true, true);
		verify(mockGinInjector, times(2)).getUserBadgeItem();
		verify(mockView, times(2)).addUserBadge(any(Widget.class));
	}
	
	@Test
	public void testAddUser() {
		list.addUserBadge(userId);
		verify(mockGinInjector).getUserBadgeItem();
		verify(mockView).addUserBadge(any(Widget.class));
	}
	
	@Test
	public void testSetDeleteTrueNoUsers() {
		list.setCanDelete(true);
		verify(mockView, times(2)).setToolbarVisible(false);
	}
	
	@Test
	public void testSetDeleteTrueWithUsers() {
		list.addUserBadge(userId);
		list.setCanDelete(true);
		verify(mockView, times(2)).setToolbarVisible(true);
	}
	
	@Test
	public void testSetDeleteFalseWithUsers() {
		list.setCanDelete(false);
		verify(mockView, times(2)).setToolbarVisible(false);	
	}
	
	@Test
	public void testRefreshListUIWithUser() {
		list.addUserBadge(userId);
		list.refreshListUI();
		verify(mockView).clearUserBadges();
		verify(mockView, times(2)).addUserBadge(any(Widget.class));
	}
	
	@Test
	public void testRefreshListUINoUsers() {
		list.refreshListUI();
		verify(mockView).clearUserBadges();
		verify(mockView, times(0)).addUserBadge(any(Widget.class));
	}
	
	@Test
	public void testDeleteSelected() {
		list.addUserBadge(userId);
		when(mockUserBadgeItem.isSelected()).thenReturn(true);
		list.deleteSelected();
		verify(mockView).clearUserBadges();
		verify(mockView, times(1)).addUserBadge(any(Widget.class)); //only called when user added initially
	}
	
	@Test
	public void testDeleteSelectedMultiple() {
		when(mockUserBadgeItem.isSelected()).thenReturn(true);
		when(mockUserBadgeItem2.isSelected()).thenReturn(false);
		when(mockGinInjector.getUserBadgeItem()).thenReturn(mockUserBadgeItem, mockUserBadgeItem2);
		list.addUserBadge(userId);
		list.addUserBadge(userId2);
		verify(mockView, times(2)).addUserBadge(any(Widget.class)); 
		reset(mockView);
		list.deleteSelected();
		verify(mockView).clearUserBadges();
		verify(mockView, times(1)).addUserBadge(any(Widget.class)); 
	}
	
	@Test
	public void testCheckSelectionStateSelected() {
		when(mockUserBadgeItem.isSelected()).thenReturn(true);
		list.setCanDelete(true);
		list.addUserBadge(userId);
		list.checkSelectionState();
		verify(mockView).setCanDelete(true);
	}
	
	@Test
	public void testCheckSelectionStateNotSelected() {
		when(mockUserBadgeItem.isSelected()).thenReturn(false);
		list.setCanDelete(true);
		list.addUserBadge(userId);
		list.checkSelectionState();
		verify(mockView).setCanDelete(false);
	}
	
	@Test
	public void testGetUserIds() {
		list.addUserBadge(userId);
		List<String> userIdList = list.getUserIds();
		assertTrue(userIdList.size() == 1);
		assertTrue(userIdList.contains(userId));
	}
	
	@Test
	public void testGetUserIdsAfterDeleting() {
		when(mockGinInjector.getUserBadgeItem()).thenReturn(mockUserBadgeItem, mockUserBadgeItem2);
		when(mockUserBadgeItem.isSelected()).thenReturn(true);
		when(mockUserBadgeItem2.isSelected()).thenReturn(false);
		
		list.addUserBadge(userId);
		list.addUserBadge(userId2);
		list.deleteSelected();
		List<String> userIdList = list.getUserIds();
		assertTrue(userIdList.size() == 1);
		assertTrue(userIdList.contains(userId2));
	}

	@Test
	public void testSelectAll() {
		when(mockGinInjector.getUserBadgeItem()).thenReturn(mockUserBadgeItem, mockUserBadgeItem2);
		list.addUserBadge(userId);
		list.addUserBadge(userId2);
		list.selectAll();
		verify(mockUserBadgeItem).setSelected(true);
		verify(mockUserBadgeItem2).setSelected(true);
	}
	
	@Test
	public void testSelectNone() {
		when(mockGinInjector.getUserBadgeItem()).thenReturn(mockUserBadgeItem, mockUserBadgeItem2);
		list.addUserBadge(userId);
		list.addUserBadge(userId2);
		list.selectNone();
		verify(mockUserBadgeItem).setSelected(false);
		verify(mockUserBadgeItem2).setSelected(false);
	}
	
}
