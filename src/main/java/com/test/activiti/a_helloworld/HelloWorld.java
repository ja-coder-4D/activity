package com.test.activiti.a_helloworld;

import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


public class HelloWorld {

	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 部署流程定义
     *
     */   
	@Test
	public void  testDeploymentProcessDefinition(){
    	Deployment deployment= processEngine.getRepositoryService()//与流程定义和部署相关的service
    	             .createDeployment()//创建部署对象
    	             .name("helloworld入门程序")
    	             .addClasspathResource("diagrams/helloworld.bpmn")
    	             .addClasspathResource("diagrams/helloworld.png")
    	             .deploy();
    	System.out.println("流程部署ID deployment.getId()"+deployment.getId());
    	System.out.println("deployment.getCategory()"+deployment.getCategory());
    	System.out.println("流程部署名字 deployment.getName()"+deployment.getName());
    	System.out.println("deployment.getClass()"+deployment.getClass());
    	System.out.println("deployment.getDeploymentTime()"+deployment.getDeploymentTime());
	}
    /**
     * 启动流程实例
     */
	@Test
    public void testStartProcessInstance(){
		String processDefinitionKey = "helloworld";
		ProcessInstance processInstance =  processEngine.getRuntimeService()
		                                 .startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例Id "+processInstance.getId());
		System.out.println("流程定义Id "+processInstance.getProcessDefinitionId());
    }
	/**
	 * 查询当前人的个人业务
	 */
	@Test
	public void testFindMyPersonTask(){
		String assignee = "张三";
		List<Task> list =  processEngine.getTaskService()
		              .createTaskQuery().taskAssignee(assignee).list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("**************************************************");
				System.out.println("任务ID : " + task.getId());
				System.out.println("任务名称 : " + task.getName());
				System.out.println("任务创建时间 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(task.getCreateTime()));
				System.out.println("任务办理人 : " + task.getAssignee());
				System.out.println("流程实例ID : " + task.getProcessInstanceId());
				System.out.println("执行对象ID : " + task.getExecutionId());
				System.out.println("流程定义ID : " + task.getProcessDefinitionId());
				System.out.println("**************************************************");
			}
		}
	}
	
	@Test
	public void testCompleteMyPersponTask(){
		
		String taskId ="602";
		processEngine.getTaskService().complete(taskId);
		System.out.println("任务完成      任务id是" + taskId);
	}
	
	
}
