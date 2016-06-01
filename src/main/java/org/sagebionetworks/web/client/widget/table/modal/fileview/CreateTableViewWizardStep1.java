package org.sagebionetworks.web.client.widget.table.modal.fileview;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.table.FileView;
import org.sagebionetworks.repo.model.table.TableEntity;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.widget.table.modal.fileview.CreateTableViewWizard.TableType;
import org.sagebionetworks.web.client.widget.table.modal.wizard.ModalPage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * First page of table/view creation wizard.  Ask for the name and scope, then create the entity.
 * @author Jay
 *
 */
public class CreateTableViewWizardStep1 implements ModalPage {
	public static final String NAME_MUST_INCLUDE_AT_LEAST_ONE_CHARACTER = "Name must include at least one character.";
	
	CreateTableViewWizardStep1View view;
	SynapseClientAsync synapseClient;
	String parentId;
	ModalPresenter modalPresenter;
	EntityContainerListWidget entityContainerList;
	TableType tableType;
	
	@Inject
	public CreateTableViewWizardStep1(
			CreateTableViewWizardStep1View view,
			SynapseClientAsync synapseClient, 
			EntityContainerListWidget entityContainerList) {
		super();
		this.view = view;
		this.entityContainerList = entityContainerList;
		view.setScopeWidget(entityContainerList.asWidget());
		this.synapseClient = synapseClient;
	}
	
	/**
	 * Configure this widget before use.
	 * 
	 * @param parentId
	 */
	public void configure(String parentId, TableType type) {
		this.parentId = parentId;
		this.tableType = type;
		boolean canEdit = true;
		view.setScopeWidgetVisible(TableType.view.equals(type));
		entityContainerList.configure(new ArrayList<String>(), canEdit);
	}
	
	/**
	 * Create the file view.
	 * @param name
	 */
	private void createFileViewEntity(final String name) {
		modalPresenter.setLoading(true);
		Entity table;
		if (TableType.view.equals(tableType)) {
			table = new FileView();
			List<String> scopeIds = entityContainerList.getEntityIds();
			((FileView)table).setScopeIds(scopeIds);
			table.setEntityType(FileView.class.getName());
		} else {
			table = new TableEntity();
			table.setEntityType(TableEntity.class.getName());
		}
		table.setName(name);
		table.setParentId(parentId);
		synapseClient.createEntity(table, new AsyncCallback<Entity>() {
			@Override
			public void onSuccess(Entity table) {
				modalPresenter.onFinished();
				//TODO: add other pages
//				modalPresenter.setLoading(false);
//				modalPresenter.setNextActivePage(next);
			}
			@Override
			public void onFailure(Throwable caught) {
				modalPresenter.setErrorMessage(caught.getMessage());
				modalPresenter.setLoading(false);
			}
		});
	}

	/**
	 * Should be Called when the create button is clicked on the dialog.
	 */
	@Override
	public void onPrimary() {
		String tableName = view.getName();
		if(tableName == null || "".equals(tableName)){
			modalPresenter.setErrorMessage(NAME_MUST_INCLUDE_AT_LEAST_ONE_CHARACTER);
		}else{
			// Create the table
			createFileViewEntity(tableName);
		}
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}

	@Override
	public void setModalPresenter(ModalPresenter modalPresenter) {
		this.modalPresenter = modalPresenter;
	}


}