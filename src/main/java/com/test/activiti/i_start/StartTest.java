package com.test.activiti.i_start;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class StartTest {

	ProcessEngine processEngine =ProcessEngines.getDefaultProcessEngine();
	/**部署流程定义（从inputStream）+ 启动流程实例+判断流程是否结束+查询历史*/
	@Test
	public void startProcessInstance(){
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("start.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("start.png");
		/**部署流程*/
		Deployment deployment = processEngine.getRepositoryService()
		             .createDeployment()
		             .name("开始活动")
		             .addInputStream("start.bpmn", inputStreamBpmn)
		             .addInputStream("start.png", inputStreamPng)
		             .deploy();
		System.out.println("流程部署id : "+ deployment.getId());
		System.out.println("流程部署名称 : "+ deployment.getName());
		/**查询流程部署的key 根据部署id查询*/
		ProcessDefinition prod = processEngine.getRepositoryService()
		             .createProcessDefinitionQuery()
		             .deploymentId(deployment.getId())
		             .singleResult();
		System.out.println("流程的key : " + prod.getKey());
		/**启动流程实例*/
		ProcessInstance startProcessInstance = processEngine.getRuntimeService()
		           .startProcessInstanceByKey(prod.getKey());
		System.out.println("流程实例ID:"+startProcessInstance.getId());//流程实例ID    
		System.out.println("流程定义ID:"+startProcessInstance.getProcessDefinitionId());//流程定义ID  
		System.out.println("流程定义ID:"+startProcessInstance.getActivityId());//流程定义ID  
	    /**查询流程是否结束*/
		
		ProcessInstance prInstance = processEngine.getRuntimeService()
		         .createProcessInstanceQuery()
		         .processInstanceId(startProcessInstance.getId())
		         .singleResult();
	
		/**流程结束*/
		if (prInstance == null) {
			/**流程结束，查询历史*/
			HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
			              .createHistoricProcessInstanceQuery()
			              .processInstanceId(startProcessInstance.getId())
			              .singleResult();
			System.out.println(historicProcessInstance.getId()+"    "+historicProcessInstance.getStartTime()+"   "+historicProcessInstance.getEndTime()+"   "+historicProcessInstance.getDurationInMillis());
		}
	} 
	
}
