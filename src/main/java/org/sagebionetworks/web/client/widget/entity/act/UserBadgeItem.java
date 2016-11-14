package org.sagebionetworks.web.client.widget.entity.act;

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.widget.SelectableListItem;
import org.sagebionetworks.web.client.widget.user.BadgeSize;
import org.sagebionetworks.web.client.widget.user.UserBadge;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Widget representing a file handle, on click will call back with the file handle id.
 * If isSelectable then will show a checkbox.
 * @author jayhodgson
 *
 */
public class UserBadgeItem implements IsWidget, SelectableListItem {
	public interface UserBadgeItemUiBinder extends UiBinder<Widget, UserBadgeItem> {}
	
	@UiField
	CheckBox select;
	@UiField
	Div userBadgeContainer;
	
	Widget widget;
	
	String fileHandleId;
	Callback selectionChangedCallback;
	PortalGinInjector portalGinInjector;
	@Inject
	public UserBadgeItem(UserBadgeItemUiBinder binder,
			PortalGinInjector portalGinInjector) {
		widget = binder.createAndBindUi(this);
		this.portalGinInjector = portalGinInjector;
		select.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectionChangedCallback != null) {
					selectionChangedCallback.invoke();
				}
			}
		});
	}
	
	public UserBadgeItem configure(String ownerId) {
		UserBadge userBadge = portalGinInjector.getUserBadgeWidget();
		userBadge.configure(ownerId);
		userBadge.setSize(BadgeSize.SMALL);
		userBadge.setCustomClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setSelected(!isSelected());
				if (selectionChangedCallback != null) {
					selectionChangedCallback.invoke();
				}
			}
		});
		userBadgeContainer.add(userBadge.asWidget());
		return this;
	}
	
	public UserBadgeItem setSelectionChangedCallback(Callback callback) {
		selectionChangedCallback = callback;
		return this;
	}
	
	public boolean isSelected() {
		return select.getValue();
	}
	
	public void setSelected(boolean selected){
		select.setValue(selected, true);
	}
	
	public void setSelectVisible(boolean visible) {
		select.setVisible(visible);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
}

