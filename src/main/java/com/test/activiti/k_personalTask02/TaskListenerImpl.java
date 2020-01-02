package com.test.activiti.k_personalTask02;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener,ExecutionListener,JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2152130484651806841L;

	@Override
	public void notify(DelegateTask delegateTask) {
            delegateTask.setAssignee("±¦±¦");
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
