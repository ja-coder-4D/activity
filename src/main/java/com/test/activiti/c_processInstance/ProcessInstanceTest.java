package com.test.activiti.c_processInstance;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/** 流程部署 */
	@Test
	public void processDefinition_ZipTest() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程定义")
				.addZipInputStream(new ZipInputStream(inputStream)).deploy();
		System.out.println("流程部署ID : " + deployment.getId());
		System.out.println("流程部署名称 : " + deployment.getName());
	}

	/** 启动流程 */
	@Test
	public void processDifinitionStratTest() {
		String processDefinitionKey = "helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程定义ID : " + processInstance.getProcessInstanceId());
		System.out.println("流程实例ID : " + processInstance.getId());
	}

	/** 查询本人的任务 */
	@Test
	public void findMyPersonProcessTest() {
		String assignee = "王五";
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

	/** 完成任务 */
	@Test
	public void completeTaskTest() {
		// 1404
		String taskId = "1602";
		processEngine.getTaskService().complete(taskId);
		System.out.println("任务完成! 任务Id 是" + taskId);
	}

	/** 查询流程状态（判断流程正在执行，还是结束） */
	@Test
	public void findProcessStatus() {
		String processInstanceId = "1401";
		ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			System.out.println("流程已经结束!");
		} else {
			System.out.println("流程未结束!");
		}
	}

	/** 查询历史任务（简） */
	@Test
	public void findHistoryTaskTest() {
		String taskAssignee = "李四";
		List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
				.taskAssignee(taskAssignee).list();
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance historicTaskInstance : list) {
				System.out.println("**************************************");
				System.out.println("TaskDefinitionKey : " + historicTaskInstance.getTaskDefinitionKey());
				System.out.println("ID  : " + historicTaskInstance.getId());
				System.out.println("taskAssignee  : " + historicTaskInstance.getAssignee());
				System.out.println("StartTime : " + historicTaskInstance.getStartTime());
				System.out.println("EndTime : " + historicTaskInstance.getEndTime());
				System.out.println("Name : " + historicTaskInstance.getName());
				System.out.println("**************************************");
			}
		}
	}

	/** 查询历史流程实例（简） */
	@Test
	public void findHistoryProcessInstanceTest() {
		String processInstanceId = "1401";
		HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (historicProcessInstance != null) {
			System.out.println("Id :" + historicProcessInstance.getId());
			System.out.println("StartUserId :" + historicProcessInstance.getStartUserId());
			System.out.println("ProcessDefinitionId :" + historicProcessInstance.getProcessDefinitionId());
		}
	}
}
