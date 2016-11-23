package org.sagebionetworks.web.client.widget.entity.act;

import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.sagebionetworks.web.client.DisplayUtils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ApproveConfirmationModalViewImpl implements ApproveConfirmationModalView {
	
	public interface Binder extends UiBinder<Widget, ApproveConfirmationModalViewImpl> {}
	private static Binder uiBinder = GWT.create(Binder.class);
	
	public static final String ACCESS_REQ_STR = "Access Requirement: ";
	public static final String REJECT_TITLE = "Reject Access Request";
	public static final String APPROVE_TITLE = "Approve Access Request";
	
	@UiField
	Modal modal;
	@UiField
	HTML accessReqText;
	@UiField
	Heading accessReqNum;
	@UiField
	Div synAlertContainer;
	@UiField
	Button submitButton;
	@UiField
	Button cancelButton;
	@UiField
	Button previewButton;
	@UiField
	TextArea messageEditArea;
	@UiField
	Div usersContainer;
	@UiField
	Div loadingEmail;
	@UiField
	Modal previewModal;
	@UiField
	HTML messageBody;
	@UiField
	Button closeButton;
	
	private Presenter presenter;
	
	Widget widget;

	public ApproveConfirmationModalViewImpl() {
		widget = uiBinder.createAndBindUi(this);
		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.onSubmit();
			}
		});
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
			}
		});
		previewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				messageBody.setHTML(messageEditArea.getText());
				previewModal.show();
			}
		});
		closeButton.addClickHandler(new ClickHandler() {		
			@Override		
			public void onClick(ClickEvent event) {		
				previewModal.hide();		
			}		
		});
	}
	
	@Override
	public void setState(boolean approve) {
		if (approve) {
			modal.setTitle(APPROVE_TITLE);
		} else {
			modal.setTitle(REJECT_TITLE);
		}
	}
	
	@Override
	public String getEmailMessage() {
		return messageEditArea.getText();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setAccessReqNumber(Long num) {
		accessReqNum.setText(ACCESS_REQ_STR + Long.toString(num));
	}
	
	@Override
	public void setLoadingEmailWidget(Widget w) {
		loadingEmail.clear();
		loadingEmail.add(w.asWidget());
	}
	
	@Override
	public void startLoadingEmail() {
		previewButton.setVisible(false);
		setLoadingEmailVisible(true);
	}
	
	@Override
	public void finishLoadingEmail() {
		setLoadingEmailVisible(false);
		previewButton.setVisible(true);
	}
	
	@Override
	public void setLoadingEmailVisible(boolean visible) {
		loadingEmail.clear();
		loadingEmail.setVisible(visible);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void show() {
		modal.show();
	}
	
	@Override
	public void hide() {
		modal.hide();
	}
	
	@Override
	public void showInfo(String title, String message) {
		DisplayUtils.showInfo(title, message);
	}

	@Override
	public void setApproveProcessing(boolean processing) {
		if(processing){
			submitButton.state().loading();
		}else{
			submitButton.state().reset();
		}
		cancelButton.setEnabled(!processing);
	}
	
	@Override
	public void setAccessRequirement(String html) {
		accessReqText.setHTML(html);
	}

	@Override
	public void setSynAlert(Widget widget) {
		synAlertContainer.clear();
		synAlertContainer.add(widget.asWidget());		
	}
	
	@Override
	public Widget getEmailBodyWidget(String html) {
		HTML display = new HTML();
		display.setHTML(html);
		return display.asWidget();
	}
	
	@Override		
	public void setMessageBody(String html) {		
		messageBody.getElement().setInnerHTML(html);
	}
	
	@Override
	public void setMessageEditArea(String html) {
		messageEditArea.setText(html);
	}

	@Override
	public String getAccessRequirement() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setUserListWidget(Widget w) {
		usersContainer.clear();
		usersContainer.add(w.asWidget());
	}
}
