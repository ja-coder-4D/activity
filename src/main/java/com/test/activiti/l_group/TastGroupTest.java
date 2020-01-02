package com.test.activiti.l_group;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**组任务*/
public class TastGroupTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程*/
	@Test
	public void StratProcessTest() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("task.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("task.png");
		Deployment deploy = processEngine.getRepositoryService()
		         .createDeployment()
		         .name("组任务")
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
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userIds", "大大,小小,中中");
		ProcessInstance processInstance = processEngine.getRuntimeService()
		              .startProcessInstanceByKey(processDefinitionKey,variables);
		System.out.println("流程定义ID : " + processInstance.getProcessInstanceId());
		System.out.println("流程实例ID : " + processInstance.getId());
	}
	
	/**查询当前人的个人任务*/
	@Test
	public void findMyPersonTask(){
		String assignee = "大大";
		List<Task> list = processEngine.getTaskService()
		             .createTaskQuery()
		             .taskAssignee(assignee)
		             .orderByTaskCreateTime()
		             .desc()
		             .list();
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
	
	/**查询当前人的组任务*/
	@Test
	public void findGroupTask(){
		String candidateUser = "大大";
		List<Task> list = processEngine.getTaskService()
		             .createTaskQuery()
		             .taskCandidateUser(candidateUser)
		             .orderByTaskCreateTime()
		             .desc()
		             .list();
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
	
	
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		//任务ID
		String taskId = "6905";
		processEngine.getTaskService()//与正在执行的任务管理相关的Service
					.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	/**查询正在执行的任务办理人表*/
	@Test
	public void findRunPersonTask() {
		String  taskId = "";
	    List<IdentityLink> identityLinksForTask = processEngine.getTaskService().getIdentityLinksForTask(taskId);
	    if (identityLinksForTask != null && identityLinksForTask.size() >0) {
	    	for (IdentityLink identityLink : identityLinksForTask) {
	    		System.out.println(identityLink.getTaskId()+"   "+identityLink.getType()+"   "+identityLink.getProcessInstanceId()+"   "+identityLink.getUserId());
			}
	    }
	
	}
	/**查询历史任务的办理人表*/
	@Test
	public void findHistoryPersonTask(){
		String processInstanceId ="";
		List<HistoricIdentityLink> historicIdentityLinksForProcessInstance = processEngine.getHistoryService()
				             .getHistoricIdentityLinksForProcessInstance(processInstanceId);
		if (historicIdentityLinksForProcessInstance != null && historicIdentityLinksForProcessInstance.size() > 0 ) {
			for (HistoricIdentityLink historicIdentityLink : historicIdentityLinksForProcessInstance) {
				System.out.println(historicIdentityLink.getTaskId()+"   "+historicIdentityLink.getType()+"   "+historicIdentityLink.getProcessInstanceId()+"   "+historicIdentityLink.getUserId());
			}
		}
	}
	
	/**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
	@Test
	public void claim (){
		//将组任务分配给个人任务
		//任务ID
		 String taskId = "";
		 //分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		 String userId = "";
		 processEngine.getTaskService().claim(taskId, userId);
	}
	
	
	/**将个人任务回退到组任务，前提，之前一定是个组任务*/
	@Test
	public void setAssignee() {
		String taskId = "";
		processEngine.getTaskService().setAssignee(taskId, "");
	}
	
	/**向组任务中添加成员*/
	@Test
	public void addGroupUser(){
		//任务ID
		String taskId = "";
		//业务办理人
		String userId = "";
		processEngine.getTaskService().addCandidateUser(taskId, userId);
	}
	/**从组任务中删除成员*/
	public void deleteGroupUser(){
		//任务ID
		String taskId = "";
		//业务办理人
		String userId = "";
		processEngine.getTaskService().deleteCandidateUser(taskId, userId);
	}
	
}
