package com.test.activiti.h_parallelGateWay;

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

public class ParallelGateWayTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**流程部署*/
	@Test
	public void processDefinitionDeployment_InputStream(){
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("parallelGateWay.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("parallelGateWay.png");
		Deployment deploy = processEngine.getRepositoryService()
		         .createDeployment()
		         .name("并行网关")
		         .addInputStream("parallelGateWay.bpmn", inputStreamBpmn)
		         .addInputStream("parallelGateWay.png", inputStreamPng)
		         .deploy();
		System.out.println("流程部署ID :" +deploy.getId());
		System.out.println("流程部署名称 :" +deploy.getName());
	}
	
	/**流程启动*/
	@Test
	public void processDifinitionStratTest(){
		String processDefinitionKey ="parallelGateWay";
		ProcessInstance processInstance = processEngine.getRuntimeService()
		              .startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程定义ID : " + processInstance.getProcessInstanceId());
		System.out.println("流程实例ID : " + processInstance.getId());
	}
	/**查询本人的任务*/
	@Test
	public void findMyPersonTaskTest(){
		String assignee = "买家";
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
	@Test
	public void completeTaskTest(){
		String taskId = "4302";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("money", 888);
		processEngine.getTaskService().complete(taskId, variables);
		System.out.println("任务完成! 任务id是:  " + taskId);
	}
}
