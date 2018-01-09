package com.nway.platform.workflow.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteLisntener implements ApplicationListener<TaskCompleteEvent> {

	@Override
	public void onApplicationEvent(TaskCompleteEvent event) {

		
	}

}
