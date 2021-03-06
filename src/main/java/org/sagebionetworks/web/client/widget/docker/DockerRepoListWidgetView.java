package org.sagebionetworks.web.client.widget.docker;

import org.sagebionetworks.repo.model.EntityBundle;
import org.sagebionetworks.web.client.widget.pagination.PaginationWidget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface DockerRepoListWidgetView extends IsWidget {

	public interface Presenter {

		void onClickAddExternalRepo();

		void onRepoClicked(EntityBundle bundle);
	}

	void addPaginationWidget(PaginationWidget paginationWidget);

	void showPaginationVisible(boolean visible);

	void setAddExternalRepoButtonVisible(boolean visibile);

	void addExternalRepoModal(IsWidget addExternalRepoModel);

	void setPresenter(Presenter presenter);

	void setSynAlert(Widget widget);

	void setSynAlertVisible(boolean visible);

	void clear();

	void addRepo(EntityBundle bundle);
}
