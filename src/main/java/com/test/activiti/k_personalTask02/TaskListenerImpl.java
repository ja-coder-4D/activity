package com.test.activiti.k_personalTask02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2152130484651806841L;

	@Override
	public void notify(DelegateTask delegateTask) {

            delegateTask.setAssignee("±¦±¦");
	}

}
