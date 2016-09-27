package org.sagebionetworks.web.client.widget.clienthelp;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FileClientsHelpImpl implements FileClientsHelp {
	@UiField
	SpanElement id1;
	@UiField
	SpanElement id2;
	@UiField
	SpanElement id3;
	@UiField
	SpanElement id4;
	@UiField
	SpanElement id5;
	@UiField
	SpanElement id6;
	@UiField
	Anchor link;
	@UiField
	Modal modal;
	@UiField
	TabListItem cliTabListItem;
	@UiField
	TabListItem pythonTabListItem;
	@UiField
	TabListItem rTabListItem;
	@UiField
	TabPane cliTabPane;
	@UiField
	TabPane pythonTabPane;
	@UiField
	TabPane rTabPane;
	
	Widget widget;
	String entityId = null;
	public interface Binder extends UiBinder<Widget, FileClientsHelpImpl> {}

	@Inject
	public FileClientsHelpImpl(Binder binder) {
		this.widget = binder.createAndBindUi(this);
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.show();
			}
		});
		
		FileClientsHelpImpl.setId(cliTabListItem, cliTabPane);
		FileClientsHelpImpl.setId(pythonTabListItem, pythonTabPane);
		FileClientsHelpImpl.setId(rTabListItem, rTabPane);
	}
	
	public static void setId(TabListItem tabListItem, TabPane tabPane) {
		String id = HTMLPanel.createUniqueId();
		tabListItem.setDataTarget("#"+id);
		tabPane.setId(id);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void configure(String entityId) {
		this.entityId = entityId;
		id1.setInnerHTML(entityId);
		id2.setInnerHTML(entityId);
		id3.setInnerHTML(entityId);
		id4.setInnerHTML(entityId);
		id5.setInnerHTML(entityId);
		id6.setInnerHTML(entityId);
	}
}
