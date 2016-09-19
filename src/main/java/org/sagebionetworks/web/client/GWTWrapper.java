package org.sagebionetworks.web.client;

import java.util.Date;

import org.sagebionetworks.web.client.utils.Callback;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.xhr.client.XMLHttpRequest;

public interface GWTWrapper {
	String getHostPageBaseURL();

	String getModuleBaseURL();

	void assignThisWindowWith(String url);

	String encodeQueryString(String queryString);
	String decodeQueryString(String queryString);

	XMLHttpRequest createXMLHttpRequest();

	NumberFormat getNumberFormat(String pattern);
	
	String getHostPrefix();
	
	String getCurrentURL();
	
	DateTimeFormat getDateTimeFormat(PredefinedFormat format);
	
	void scheduleFixedDelay(final Callback callback, int delayMs);
	
	void scheduleExecution(Callback callback, int delay);
	
	void scheduleDeferred(Callback callback);
	
	String getUserAgent();
	
	String getAppVersion();
	
	int nextRandomInt();

	String getFormattedDateString(Date date);
	
	void addDaysToDate(Date date, int days);
	
	boolean isWhitespace(String text);

	void newItem(String historyToken, boolean issueEvent);

	void replaceItem(String historyToken, boolean issueEvent);
	
	ServiceDefTarget asServiceDefTarget(Object service);
	HasRpcToken asHasRpcToken(Object service);

	String getUniqueElementId();
}
