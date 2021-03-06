package org.sagebionetworks.web.client.widget.entity.file;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.html.Span;
import org.sagebionetworks.web.client.DisplayUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FileDownloadButtonViewImpl implements FileDownloadButtonView {

	private Presenter presenter;
	
	@UiField
	Anchor licensedDownloadLink;
	@UiField
	Anchor licensedDownloadLink2;
	@UiField
	Anchor directDownloadLink;
	@UiField
	Anchor directDownloadLink2;
	@UiField
	Anchor authorizedDirectDownloadLink;
	@UiField
	Anchor authorizedDirectDownloadLink2;
	@UiField
	Span clientsHelpContainer;
	@UiField
	Span synAlertContainer;
	@UiField
	ButtonElement directDownloadButton;
	@UiField
	ButtonElement authorizedDownloadButton;
	@UiField
	ButtonElement licensedDownloadButton;
	boolean isExtraSmall;
	interface FileDownloadButtonViewImplUiBinder extends UiBinder<Widget, FileDownloadButtonViewImpl> {}

	private static FileDownloadButtonViewImplUiBinder uiBinder = GWT.create(FileDownloadButtonViewImplUiBinder.class);
	Widget widget;
	@Inject
	public FileDownloadButtonViewImpl() {
		widget = uiBinder.createAndBindUi(this);
		ClickHandler licensedDownloadClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//if there is an href, ignore it
				event.preventDefault();
				presenter.onLicensedDownloadClick();
			}
		};
		licensedDownloadLink.addClickHandler(licensedDownloadClickHandler);
		licensedDownloadLink2.addClickHandler(licensedDownloadClickHandler);
		
		ClickHandler authorizedDirectDownloadClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onAuthorizedDirectDownloadClicked();
			}
		};
		authorizedDirectDownloadLink.addClickHandler(authorizedDirectDownloadClickHandler);
		authorizedDirectDownloadLink2.addClickHandler(authorizedDirectDownloadClickHandler);
		isExtraSmall = false;
	}
	
	@Override
	public void clear() {
		licensedDownloadLink.setVisible(false);
		directDownloadLink.setVisible(false);
		authorizedDirectDownloadLink.setVisible(false);
		licensedDownloadLink2.setVisible(false);
		directDownloadLink2.setVisible(false);
		authorizedDirectDownloadLink2.setVisible(false);
	}
	
	@Override
	public void setClientsHelpVisible(boolean visible) {
		clientsHelpContainer.setVisible(visible);
	}
	
	@Override
	public void setDirectDownloadLink(String href) {
		directDownloadLink.setHref(href);
		directDownloadLink2.setHref(href);
	}
	
	@Override
	public void setAuthorizedDirectDownloadLinkVisible(boolean visible) {
		authorizedDirectDownloadLink.setVisible(visible && !isExtraSmall);
		authorizedDirectDownloadLink2.setVisible(visible && isExtraSmall);
	}
	
	@Override
	public void setDirectDownloadLinkVisible(boolean visible) {
		directDownloadLink.setVisible(visible && !isExtraSmall);
		directDownloadLink2.setVisible(visible && isExtraSmall);
	}
	@Override
	public void setLicensedDownloadLinkVisible(boolean visible) {
		licensedDownloadLink.setVisible(visible && !isExtraSmall);
		licensedDownloadLink2.setVisible(visible && isExtraSmall);
	}
	@Override
	public void setSynAlert(IsWidget w) {
		synAlertContainer.clear();
		synAlertContainer.add(w);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}	

	@Override 
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setFileClientsHelp(IsWidget w) {
		clientsHelpContainer.clear();
		clientsHelpContainer.add(w);
	}
	
	private void removeButtonSizeStyles(ButtonElement el) {
		for (ButtonSize size : ButtonSize.values()) {
			String cssName = size.getCssName();
			if (DisplayUtils.isDefined(cssName))
				el.removeClassName(cssName);
		}
	}
	
	@Override
	public void setButtonSize(ButtonSize size) {
		isExtraSmall = size.equals(ButtonSize.EXTRA_SMALL);
		removeButtonSizeStyles(directDownloadButton);
		removeButtonSizeStyles(authorizedDownloadButton);
		removeButtonSizeStyles(licensedDownloadButton);
		directDownloadButton.addClassName(size.getCssName());
		authorizedDownloadButton.addClassName(size.getCssName());
		licensedDownloadButton.addClassName(size.getCssName());
	}
}
