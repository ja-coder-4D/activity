package com.test.activiti.l_group02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListenerImpl implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3586441620175906870L;

	@Override
	public void notify(DelegateTask delegateTask) {
		//指定个人任务的办理人，也可以指定组任务的办理人
		//个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
		//delegateTask.setAssignee("灭绝师太");
		//组任务：
		delegateTask.addCandidateUser("郭靖");
		delegateTask.addCandidateUser("黄蓉");
	}

}
