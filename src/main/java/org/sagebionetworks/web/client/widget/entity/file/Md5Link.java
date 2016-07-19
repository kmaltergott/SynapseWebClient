package org.sagebionetworks.web.client.widget.entity.file;

import org.gwtbootstrap3.client.ui.constants.Placement;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.widget.CopyTextModal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class Md5Link extends SimplePanel {
	CopyTextModal copyTextModal;
	@Inject
	public Md5Link(CopyTextModal modal) {
		addStyleName("inline-block font-italic");
		this.copyTextModal = modal;
		copyTextModal.setTitle("md5:");
	}
	
	public void configure(final String md5) {
		this.clear();
		Anchor md5Link = new Anchor("md5");
		md5Link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showMd5Dialog(md5);
			}
		});
		DisplayUtils.addTooltip(md5Link, md5, Placement.BOTTOM);
		this.add(md5Link);
		this.add(copyTextModal.asWidget());
	}
	
	private void showMd5Dialog(String md5) {
		copyTextModal.setText(md5);
		copyTextModal.show();
	}
}
