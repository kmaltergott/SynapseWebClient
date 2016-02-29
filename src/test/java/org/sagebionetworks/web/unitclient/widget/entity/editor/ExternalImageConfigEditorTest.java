package org.sagebionetworks.web.unitclient.widget.entity.editor;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sagebionetworks.web.client.widget.entity.dialog.DialogCallback;
import org.sagebionetworks.web.client.widget.entity.editor.ExternalImageConfigEditor;
import org.sagebionetworks.web.client.widget.entity.editor.ExternalImageConfigView;
import org.sagebionetworks.web.shared.WikiPageKey;
public class ExternalImageConfigEditorTest {

	@Mock
	ExternalImageConfigView mockView;
	@Mock
	DialogCallback mockCallback;

	ExternalImageConfigEditor editor;
	WikiPageKey wikiKey;
	Map<String, String> descriptor;

	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		editor = new ExternalImageConfigEditor(mockView);
		editor.configure(wikiKey, descriptor, mockCallback);
	}

	@Test
	public void testAsWidget() {
		editor.asWidget();
		verify(mockView).asWidget();
	}

	@Test
	public void testConstruction() {
		verify(mockView).setPresenter(editor);
	}

	@Test
	public void testConfigure() {
		verify(mockView).initView();
	}

	@Test
	public void testTextToInsert() {
		String textToInsert = editor.getTextToInsert();
		verify(mockView).getImageUrl();
		assertTrue(textToInsert != null && textToInsert.length() > 0);
	}

}
