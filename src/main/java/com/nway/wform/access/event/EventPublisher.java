package com.nway.wform.access.event;

import java.util.HashMap;
import java.util.Map;

public class EventPublisher {

	private static final Map<String, FormDataEventListener> FORM_DATA_EVENT_LISTENER = new HashMap<String, FormDataEventListener>();
	
	public static void registFormDataEventListener(FormDataEventListener listener) {
		
		FORM_DATA_EVENT_LISTENER.put(listener.getName(), listener);
	}
	
	public static void publishFormDataEvent(FormDataEvent formDataEvent) {
		
		FORM_DATA_EVENT_LISTENER.get(formDataEvent.getSource()).handleEvent(formDataEvent);
	}
}
