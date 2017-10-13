package com.nway.wform.access.event;

import java.util.EventListener;

public abstract class FormDataEventListener implements EventListener {

	public void handleEvent(FormDataEvent formDataEvent) {
		
		if(FormDataEvent.EVENT_HANDLE_TYPE_BEFORE == formDataEvent.getHandleType()) {
			
			preEvent(formDataEvent);
		}
		else if(FormDataEvent.EVENT_HANDLE_TYPE_AFTER == formDataEvent.getHandleType()) {
			
			afterEvent(formDataEvent);
		}
	}
	
	// formPageName_groupName_{EventObject.EVENT_TYPE_*}
	protected abstract String getName();
	
	public abstract void preEvent(FormDataEvent formDataEvent);
	
	public abstract void afterEvent(FormDataEvent formDataEvent);
	
}
