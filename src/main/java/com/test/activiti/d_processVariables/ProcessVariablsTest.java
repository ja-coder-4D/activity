package com.test.activiti.d_processVariables;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

public class ProcessVariablsTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void prcessDefinition_InputStreamTest(){
		 InputStream inputStreamBpmn =  this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		 InputStream inputStreamPng =  this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		 Deployment deployment =   processEngine.getRepositoryService()
		            .createDeployment()
		            .name("流程变量")
		            .addInputStream("processVariables.bpmn", inputStreamBpmn) //使用资源文件名称 (要求 和文件名保持一致) 和输入流完成部署
		            .addInputStream("processVariables.png", inputStreamPng) //使用资源文件名称 (要求 和文件名保持一致)  和输入流完成部署
		            .deploy();
		 System.out.println("流程定义ID : "+ deployment.getId());
		 System.out.println("流程定义名称 : "+ deployment.getName());
	}
}
