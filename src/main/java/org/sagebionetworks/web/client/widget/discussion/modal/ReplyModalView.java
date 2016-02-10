package org.sagebionetworks.web.client.widget.discussion.modal;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ReplyModalView extends IsWidget{

	public interface Presenter {

		public void show();

		public void hide();

		Widget asWidget();

		void onSave();
	}

	void setPresenter(Presenter presenter);
	void showDialog();
	void hideDialog();
	String getMessageMarkdown();
	void clear();
	void setAlert(Widget w);
	void showSuccess(String title, String message);
	void showSaving();
	void resetButton();
	void setMessage(String message);
	void setModalTitle(String title);

}