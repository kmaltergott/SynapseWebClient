package org.sagebionetworks.web.client.widget.discussion;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.html.Div;
import org.sagebionetworks.repo.model.discussion.DiscussionThreadOrder;
import org.sagebionetworks.web.client.DisplayUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DiscussionThreadListWidgetViewImpl implements DiscussionThreadListWidgetView{

	public interface Binder extends UiBinder<Widget, DiscussionThreadListWidgetViewImpl> {}

	@UiField
	Column threadListContainer;
	@UiField
	Div synAlertContainer;
	@UiField
	Div threadCountAlertContainer;
	@UiField
	HTMLPanel loadMore;

	@UiField
	FocusPanel sortByReplies;
	@UiField
	FocusPanel sortByViews;
	@UiField
	FocusPanel sortByActivity;
	@UiField
	Div threadHeader;


	Widget widget;
	private DiscussionThreadListWidget presenter;

	@Inject
	public DiscussionThreadListWidgetViewImpl(Binder binder) {
		widget = binder.createAndBindUi(this);
		sortByReplies.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.sortBy(DiscussionThreadOrder.NUMBER_OF_REPLIES);
			}
		});
		sortByViews.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.sortBy(DiscussionThreadOrder.NUMBER_OF_VIEWS);
			}
		});
		sortByActivity.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.sortBy(DiscussionThreadOrder.PINNED_AND_LAST_ACTIVITY);
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setPresenter(DiscussionThreadListWidget presenter) {
		this.presenter = presenter;
	}

	@Override
	public void addThread(Widget w) {
		threadListContainer.add(w);
	}
	
	@Override
	public void clear() {
		threadListContainer.clear();
	}

	@Override
	public void setAlert(Widget w) {
		synAlertContainer.add(w);
	}

	@Override
	public void setLoadMoreVisibility(boolean visible) {
		loadMore.setVisible(visible);
	}

	@Override
	public void setThreadCountAlert(Widget w) {
		threadCountAlertContainer.clear();
		threadCountAlertContainer.add(w);
	};

	@Override
	public void setThreadHeaderVisible(boolean visible){
		threadHeader.setVisible(visible);
	}

	@Override
	public boolean isLoadMoreAttached() {
		return loadMore.isAttached();
	}

	@Override
	public boolean isLoadMoreInViewport() {
		return DisplayUtils.isInViewport(loadMore.asWidget());
	}

	@Override
	public boolean getLoadMoreVisibility() {
		return loadMore.isVisible();
	}
}
