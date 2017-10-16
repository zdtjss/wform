package com.nway.wform.access.event;

import java.util.EventObject;

public class FormEvent extends EventObject {

	public static final int EVENT_HANDLE_TYPE_BEFORE = 1;
	
	public static final int EVENT_HANDLE_TYPE_AFTER = 2;
	
	public static final int EVENT_TYPE_INSERT = 1;

	public static final int EVENT_TYPE_UPDATE = 2;
	
	public static final int EVENT_TYPE_DELETE = 3;
	
	public static final int EVENT_TYPE_VIEW = 4;
	
	public static final int EVENT_TYPE_LIST = 5;

	private int handleType;
	
	public FormEvent(Object source) {
		super(source);
	}

	public int getHandleType() {
		return handleType;
	}

	public void setHandleType(int handleType) {
		this.handleType = handleType;
	}
	
}
