package org.sagebionetworks.web.client.widget.entity.act;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ApproveConfirmationModalView extends IsWidget {

	void setPresenter(Presenter presenter);
	void setSynAlert(Widget asWidget);
	void setLoadingEmailWidget(Widget w);
	void setAccessRequirement(String num, String html);
	void setApproveProcessing(boolean processing);
	void setMessageBody(String html);
	void setMessageEditArea(String html);
	void startLoadingEmail();
	void finishLoadingEmail();
	void showInfo(String title, String message);
	void setLoadingEmailVisible(boolean visible);
	void show();
	void hide();
	/**
	 * Presenter interface
	 */
	public interface Presenter {
		void onSubmit();
	}
	String getAccessRequirement();
	String getEmailMessage();
	Widget getEmailBodyWidget(String html);

}
