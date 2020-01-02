package com.test.activiti.j_receiveTask;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**部署流程定义（从inputStream）+ 启动流程实例+设置流程变量+获取流程变量+向后执行一步*/
	@Test
	public void StartProcessTest(){
		/**部署流程*/
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("receiveTask.png");
		Deployment deploy = processEngine.getRepositoryService()
		             .createDeployment()
		             .name("receiveTast")
		             .addInputStream("receiveTask.bpmn", inputStreamBpmn)
		             .addInputStream("receiveTask.png", inputStreamPng)
		             .deploy();
		System.out.println("流程部署id : "+ deploy.getId());
		System.out.println("流程部署名称 : "+ deploy.getName());
		/**查询流程部署的key 根据部署id查询*/
		ProcessDefinition prod = processEngine.getRepositoryService()
		             .createProcessDefinitionQuery()
		             .deploymentId(deploy.getId())
		             .singleResult();
		System.out.println("流程的key : " + prod.getKey()); 
		/**启动流程实例*/
		ProcessInstance startProcessInstance = processEngine.getRuntimeService()
		           .startProcessInstanceByKey(prod.getKey());
		System.out.println("流程实例ID:"+startProcessInstance.getId());//流程实例ID    
		System.out.println("流程定义ID:"+startProcessInstance.getProcessDefinitionId());//流程定义ID  
		System.out.println("流程定义ID:"+startProcessInstance.getActivityId());//流程定义ID  
		/**查询执行对象ID*/
		Execution execution1 = processEngine.getRuntimeService()
		             .createExecutionQuery()
		             .processInstanceId(startProcessInstance.getId())
		             .activityId("receivetask1")
		             .singleResult();
		/**使用流程变量设置当日销售额，用来传递业务参数*/
		processEngine.getRuntimeService().setVariable(execution1.getId(), "当月销售额", 210000);
		/**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
		processEngine.getRuntimeService().signal(execution1.getId());
		/**查询执行对象ID*/
		Execution execution2 = processEngine.getRuntimeService()
		             .createExecutionQuery()
		             .processInstanceId(startProcessInstance.getId())
		             .activityId("receivetask2")
		             .singleResult();
		/**从流程变量中获取汇总当日销售额的值*/
		Integer value = (Integer)processEngine.getRuntimeService()//
						.getVariable(execution2.getId(), "当月销售额");
		System.out.println("给老板发送短信：金额是："+value);
		/**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
		processEngine.getRuntimeService()
						.signal(execution2.getId());
	}
}
