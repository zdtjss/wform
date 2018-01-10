package com.nway.platform.workflow.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.design.entity.PageFieldForm;
import com.nway.platform.workflow.dao.WorkFlowDaoMapper;
import com.nway.platform.workflow.entity.Handle;
import com.nway.platform.workflow.entity.Handle.SimpleUser;

@Component
public class TaskCompleteLisntener implements ApplicationListener<TaskCompleteEvent> {
	
	@Autowired
	private WorkFlowDaoMapper workFlowDao;
	
	@Autowired
	private TaskService taskService;

	@Override
	public void onApplicationEvent(TaskCompleteEvent event) {

		Handle handleInfo = event.getHandleInfo();
		Map<String, SimpleUser[]> taskUserMap = handleInfo.getHandlerTaskMap();
		Map<String, Object> formData = event.getFormData();
		List<PageFieldForm> fields = event.getPage().getFormFields();
		
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(formData.get("processInstanceId").toString()).list();
		
		for(Task task : tasks) {
			
			/*List<PvmTransition>  transitions = ((TaskEntity)task).getExecution().getActivity().getIncomingTransitions();
			
			for( PvmTransition transition : transitions) {
				
				SimpleUser[] users = taskUserMap.get(transition.getProperty("name"));
				
				if(users == null) {
					
					continue;
				}
			
				for (SimpleUser user : users) {*/
	
					Map<String, Object> workItem = new HashMap<String, Object>();
					
					workItem.put("pkId", UUID.randomUUID().toString());
					workItem.put("taskId", task.getId());
					workItem.put("formUrl", task.getFormKey());
					/*workItem.put("handlerId", user.getUserId());
					workItem.put("handlerName", user.getCnName());*/
					
					for(PageFieldForm field : fields) {
						
						if(field.isShowInWorkItem()) {
							
							workItem.put(field.getName(), formData.get(field.getName()));
						}
					}
					
					workFlowDao.createWorkItem(workItem);
				//}
			//}
		}
	}

}
