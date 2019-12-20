package com.test.activiti.d_processVariables;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablsTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**流程部署 inputStream*/
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
		 System.out.println("流程部署ID : "+ deployment.getId());
		 System.out.println("流程部署名称 : "+ deployment.getName());
	}
	/**流程启动 */
	@Test
	public void processDifinitionStratTest() {
		String processDefinitionKey = "processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程定义ID : " + processInstance.getProcessInstanceId());
		System.out.println("流程实例ID : " + processInstance.getId());
	}
	
	/**设置流程变量*/
	@Test
	public void  setVariables() {
		TaskService taskService = processEngine.getTaskService();
		//任务ID
		String  taskId = "2504";
		/**一 设置流程变量    使用基本数据类型*/
	/*	taskService.setVariable(taskId, "请假天数", 3.5);
		taskService.setVariable(taskId, "请假日期", new Date());
		taskService.setVariable(taskId, "请假原因", "回家探亲!");*/
		/**二 设置流程变量    使用javabean类型*/
		Person person  = new Person();
		person.setId(20);
		person.setName("迪士尼");
		taskService.setVariable(taskId, "人员信息(加强版)",person );
		System.out.println("设置流程变量成功!");
	}
	
	/**获取流程变量*/
	@Test
	public void getVariables() {
		TaskService taskService = processEngine.getTaskService();
		String  taskId = "2702";
		/**一 获取流程变量    使用基本数据类型*/
		/*Double days = (Double) taskService.getVariable(taskId, "请假天数");
		Date date  = (Date) taskService.getVariable(taskId, "请假日期");
		String resean =  (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数  : " + days);
		System.out.println("请假日期  : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		System.out.println("请假原因  : " + resean);*/
		/**二 获取流程变量    使用javabean类型*/
		Person person = (Person) taskService.getVariable(taskId, "人员信息(加强版)");
		System.out.println("人员ID  : " + person.getId());
		System.out.println("人员姓名  : " +  person.getName());
		
	}
	
	/**模拟设置和获取流程变量的场景*/
	public void setAndGetVariables(){
		/**流程实例 执行对象 (正在运行)*/
		RuntimeService runtimeService =  processEngine.getRuntimeService();
		/**与任务 (正在运行)*/
		TaskService taskService = processEngine.getTaskService();
		/**设置流程变量*/
		//runtimeService.setVariable(executionId, variableName, value);//表示用执行对象ID 和流程变量的名称 设置流程变量的值 一次只能设置一个值
		//runtimeService.setVariables(executionId, variables);//表示用执行对象ID 和map集合设置流程变量的值 map的key 示流程变量的名称 value是流程变量的值
		
		//taskService.setVariable(taskId, variableName, value);//表示用任务ID 和流程变量的名称 设置流程变量的值 一次只能设置一个值
		//taskService.setVariables(taskId, variables);//表示用任务ID 和map集合设置流程变量的值 map的key 示流程变量的名称 value是流程变量的值
		
		//runtimeService.startProcessInstanceByKey(processDefinitionKey, variables)//启动流程实例的同时 可以设置流程变量的值 用map
		//taskService.complete(taskId, variables);//完成任务时设置流程变量的值 用map
		
		/**获取流程变量*/
		//runtimeService.getVariable(executionId, variableName)//使用流程执行对象ID和流程变量的名称获取流程变量的值
		//runtimeService.getVariables(executionId) // 使用流程执行对象ID 获取所有的流程变量的值 将流程变量放到map中 map的key 放的是流程变量的名称， value 是流程变量的值
		//runtimeService.getVariables(executionId, variableNames)//使用流程执行对象ID 获取流程变量 将流程变量的名称放到list中 放到map
		
		//taskService.getVariable(taskId, variableName)//使用任务ID和流程变量的名称获取流程变量的值
		//taskService.getVariables(taskId) // 使用任务ID 获取所有的流程变量的值 将流程变量放到map中 map的key 放的是流程变量的名称， value 是流程变量的值
		//taskService.getVariables(taskId, variableNames)//使用任务ID 获取流程变量 将流程变量的名称放到list中 放到map
	}
	
	
	/** 完成任务 */
	@Test
	public void completeTaskTest() {
		// 1404
		String taskId = "2702";
		processEngine.getTaskService().complete(taskId);
		System.out.println("任务完成! 任务Id 是" + taskId);
	}
	
	/** 查询历史的流程变量 */
	@Test
	public void findHistoryVariablesTest(){
		 List<HistoricVariableInstance> list = processEngine.getHistoryService()
		              .createHistoricVariableInstanceQuery()
		              .variableName("请假天数")
		              .list();
		 if (list!= null && list.size() > 0) {
			for (HistoricVariableInstance historicVariableInstance : list) {
				System.out.println("*************************************************");
				System.out.print("ID : " + historicVariableInstance.getId() +"\t" + ",");
				System.out.print("流程实例ID : " + historicVariableInstance.getProcessInstanceId() +"\t"+ ",");
				System.out.print("变量名称 : " + historicVariableInstance.getVariableName()+"\t" + ",");
				System.out.print("变量类型  : " + historicVariableInstance.getVariableTypeName()+"\t"+ ",");
				System.out.println("变量值 : " + historicVariableInstance.getValue()+"\t");
				System.out.println("*************************************************");
				
			}
		}
	}
	
}
