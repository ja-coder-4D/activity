package com.test.activiti.k_personalTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
/**流程变量设置办理人*/
public class TaskTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**部署流程*/
	@Test
	public void StratProcessTest() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("task.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("task.png");
		Deployment deploy = processEngine.getRepositoryService()
		         .createDeployment()
		         .name("任务")
		         .addInputStream("task.bpmn", inputStreamBpmn)
		         .addInputStream("task.png", inputStreamPng)
		         .deploy();
		System.out.println("流程部署ID :" +deploy.getId());
		System.out.println("流程部署名称 :" +deploy.getName());
	}
	/**流程启动*/
	@Test
	public void processDifinitionStratTest(){
		String processDefinitionKey ="task";
		//流程变量设置办理人
		Map<String , Object> variables = new HashMap<String, Object>();
		variables.put("userId", "小马");
		ProcessInstance processInstance = processEngine.getRuntimeService()
		             .startProcessInstanceByKey(processDefinitionKey, variables);
		System.out.println("流程定义ID : " + processInstance.getProcessInstanceId());
		System.out.println("流程实例ID : " + processInstance.getId());
	}
	/**查询本人的任务*/
	@Test
	public void findMyPersonTaskTest(){
		String assignee = "小马";
		List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("**************************************************");
				System.out.println("任务ID : " + task.getId());
				System.out.println("任务名称 : " + task.getName());
				System.out.println(
						"任务创建时间 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getCreateTime()));
				System.out.println("任务办理人 : " + task.getAssignee());
				System.out.println("流程实例ID : " + task.getProcessInstanceId());
				System.out.println("执行对象ID : " + task.getExecutionId());
				System.out.println("流程定义ID : " + task.getProcessDefinitionId());
				System.out.println("**************************************************");
			}
		}
		
	}
	/**办理任务*/
	@Test
	public void completeTaskTest(){
		String taskId = "5905";
		processEngine.getTaskService().complete(taskId);
		System.out.println("任务完成! 任务id是:  " + taskId);
	}
}
