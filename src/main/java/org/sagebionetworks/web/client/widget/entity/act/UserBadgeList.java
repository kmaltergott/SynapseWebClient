package org.sagebionetworks.web.client.widget.entity.act;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.utils.CallbackP;
import org.sagebionetworks.web.client.widget.search.SynapseSuggestBox;
import org.sagebionetworks.web.client.widget.search.SynapseSuggestion;
import org.sagebionetworks.web.client.widget.search.UserGroupSuggestionProvider;
import org.sagebionetworks.web.client.widget.upload.FileUpload;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class UserBadgeList implements UserBadgeListView.Presenter, IsWidget {

	UserBadgeListView view;
	PortalGinInjector ginInjector;
	boolean isToolbarVisible, changingSelection; //canDelete
	List<UserBadgeItem> users;
	SynapseSuggestBox peopleSuggestWidget;	
	Callback selectionChangedCallback;
	
	@Inject
	public UserBadgeList (
			SynapseSuggestBox peopleSuggestBox,
			UserGroupSuggestionProvider provider, 
			UserBadgeListView view, 
			PortalGinInjector ginInjector) {
		super();
		this.view = view;
		this.peopleSuggestWidget = peopleSuggestBox;
		peopleSuggestWidget.setSuggestionProvider(provider);
		this.ginInjector = ginInjector;
		this.view.setPresenter(this);
		this.view.setSelectorWidget(peopleSuggestWidget.asWidget());
		users = new ArrayList<UserBadgeItem>();
		peopleSuggestBox.addItemSelectedHandler(new CallbackP<SynapseSuggestion>() {
			@Override
			public void invoke(SynapseSuggestion suggestion) {
				onUserSelected(suggestion);
			}
		});
		selectionChangedCallback = new Callback() {
			@Override
			public void invoke() {
				refreshLinkUI();
			}
		};
		
	}
	
	public void onUserSelected(SynapseSuggestion suggestion) {
		addUserBadge(suggestion.getId());
		peopleSuggestWidget.clear();
		refreshLinkUI();
	}
	
	public UserBadgeList configure(){
		this.isToolbarVisible = false;
		view.setToolbarVisible(false);
		return this;
	};
	
	/**
	 * If true then show toolbar with the delete button.
	 * @param canDelete
	 * @return
	 */
	public UserBadgeList setCanDelete(boolean canDelete) {
		this.isToolbarVisible = canDelete;
		view.setToolbarVisible(isToolbarVisible);
		return this;
	}
	
	public void addUserBadge(String userId) {
		UserBadgeItem item = ginInjector.getUserBadgeItem();
		item.configure(userId).setSelectionChangedCallback(selectionChangedCallback);
		users.add(item);
		view.addUserBadge(item.asWidget());
	}
	
	public void refreshLinkUI() {
		view.clearUserBadges();
		for (UserBadgeItem item : users) {
			view.addUserBadge(item.asWidget());
		}
		
		boolean toolbarVisible = isToolbarVisible && users.size() > 0;
		view.setToolbarVisible(toolbarVisible);
		if (toolbarVisible) {
			checkSelectionState();	
		}
	}
	
	@Override
	public void deleteSelected() {
		//remove all selected file links
		Iterator<UserBadgeItem> it = users.iterator();
		while(it.hasNext()){
			UserBadgeItem row = it.next();
			if(row.isSelected()){
				it.remove();
			}
		}
		refreshLinkUI();
	}
	
	/**
	 * Change the selection state of all rows to the passed value.
	 * 
	 * @param select
	 */
	private void changeAllSelection(boolean select){
		try{
			changingSelection = true;
			// Select all
			for (UserBadgeItem item : users) {
				item.setSelected(select);
			}
		}finally{
			changingSelection = false;
		}
		checkSelectionState();
	}
	

	/**
	 * The current selection state determines which buttons are enabled.
	 */
	public void checkSelectionState(){
		if(!changingSelection && isToolbarVisible){
			int count = 0;
			for (UserBadgeItem item : users) {
				count += item.isSelected() ? 1 : 0;
			}
			view.setCanDelete(count > 0);
		}
	}
	
	@Override
	public void selectAll() {
		changeAllSelection(true);
	}
	
	@Override
	public void selectNone() {
		changeAllSelection(false);
	}
	
	@Override
	public Widget asWidget() {
		return view.asWidget();
	}

	@Override
	public List<String> getUserBadges() {
		// TODO Auto-generated method stub
		return null;
	}
}