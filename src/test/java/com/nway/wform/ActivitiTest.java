package com.nway.wform;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class ActivitiTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;

	@Test
	@Deployment
	public void simpleProcessTest() {
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("abcprocess");
		
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).active().singleResult();
		
		System.out.println(task.getId() +" \t "+task.getName());
		
		taskService.complete(task.getId());
		
		task = taskService.createTaskQuery().processInstanceId(pi.getId()).active().singleResult();
		
		System.out.println(task.getId() +" \t "+task.getName());
		
		pi = runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
		
		System.out.println(pi == null);
		
		taskService.complete(task.getId());
		
		pi = runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
		
		System.out.println(pi == null);
		
		System.out.println(runtimeService.createProcessInstanceQuery().count());

	}

}
