package com.test.activiti.e_HistoryQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQueryTest {

	ProcessEngine  processEngine = ProcessEngines.getDefaultProcessEngine();
	
	
	
	
	/**查询历史流程实例*/
	@Test
	public void findHistoryProcessInstancetest(){
		String processInstanceId = "2501";
		List<HistoricProcessInstance> list = processEngine.getHistoryService()
		             .createHistoricProcessInstanceQuery()
		             .processInstanceId(processInstanceId)
		             .orderByProcessInstanceStartTime().asc()
		             .list();
		if (list != null && list.size() > 0 ) {
			for (HistoricProcessInstance historicProcessInstance : list) {
				System.out.println("ProcessDefinitionId : " + historicProcessInstance.getProcessDefinitionId());
				System.out.println("StartTime : " + historicProcessInstance.getStartTime());
				System.out.println("EndTime : " + historicProcessInstance.getEndTime());
			}
		}	             
	}
	
	
	/**查询历史活动*/
	
	@Test
	public void findHistoryActivityTest(){
		String processInstanceId = "2501";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
		             .createHistoricActivityInstanceQuery()
		             .processInstanceId(processInstanceId)
		             .orderByHistoricActivityInstanceStartTime()
		             .asc()
		             .list();
		if (list != null && list.size() > 0) {
			for (HistoricActivityInstance historicActivityInstance : list) {
				System.out.println("********************************************");
				System.out.println("ProcessDefinitionId : " +historicActivityInstance.getProcessDefinitionId());
				System.out.println("ActivityName : " + historicActivityInstance.getActivityName());
				System.out.println("Assignee : " + historicActivityInstance.getAssignee());
				System.out.println("********************************************");
			}
		}
	}
	
	/**查询历史任务*/
	@Test
	public void findHistoryTaskTest(){
		String processInstanceId = "2501";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
		             .createHistoricTaskInstanceQuery()
		             .processInstanceId(processInstanceId)
		             .orderByHistoricTaskInstanceStartTime()
		             .asc()
		             .list();
		if (list != null && list.size() > 0 ) {
			for (HistoricTaskInstance historicTaskInstance : list) {
				System.out.println("********************************************");
				System.out.println("ProcessDefinitionId : " +historicTaskInstance.getProcessDefinitionId());
				System.out.println("ActivityName : " + historicTaskInstance.getName());
				System.out.println("Assignee : " + historicTaskInstance.getAssignee());
				System.out.println("********************************************");
			}
		}
	}
	
	/**查询历史流程变量*/
	@Test
	public void findHistoryVariables(){
		String processInstanceId = "2501";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
		             .createHistoricVariableInstanceQuery()
		             .processInstanceId(processInstanceId)
		             .list();
		
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance historicVariableInstance : list) {
				System.out.println( "VariableName  : " + historicVariableInstance.getVariableName());
				System.out.println( "VariableTypeName  : " + historicVariableInstance.getVariableTypeName());
			}
		}
	}
}
