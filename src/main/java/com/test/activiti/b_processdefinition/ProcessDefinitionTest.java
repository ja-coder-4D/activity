package com.test.activiti.b_processdefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	
	/**
	 * 流程部署定义 classpath
	 */
	@Test
	public void prcessDefinition_ClassPathTest() {
		Deployment deploy = processEngine.getRepositoryService()
				.createDeployment().name("流程定义")
				.addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.bpmn")
				.name("流程定义_ClASSPATH").deploy();
		System.out.println("流程部署ID : " + deploy.getId());
		System.out.println("流程部署名称  : " + deploy.getName());
	}

	/**
	 * 流程部署定义 zip
	 */
	@Test
	public void processDefinition_ZipTest() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		Deployment deploy = processEngine.getRepositoryService()
				.createDeployment()
				.addZipInputStream(new ZipInputStream(inputStream)).name("流程定义_ZIP").deploy();
		System.out.println("流程定义ID : " + deploy.getId());
		System.out.println("流程名称  : " + deploy.getName());
	}

	/**
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinitionTest() {
		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService().createProcessDefinitionQuery() // 创建一个流程定义的查询
				/** 指定查询条件 */
				// .deploymentId("1101")//使用部署对象的ID查询 1101
				// .processDefinitionId("helloworld:5:1104")//使用流程定义id查询
				// .processDefinitionKey("helloworld")//使用流程定义key查询
				// .processDefinitionNameLike("helloworld%")//使用流程定义名字模糊查询
				// .suspended()//挂起 (查询不到)
				/** 排序 */
				.orderByProcessDefinitionVersion().asc()// 根据版本升序排序
				// .orderByProcessDefinitionName().desc() //根据流程定义名字降序排序
				/** 返回的结果集 */
				.list();// 返回一个结果集，封装流程定义的对象
		// .singleResult()//返回唯一结果集
		// .count() //返回结果集数量
		// .listPage(firstResult, maxResults)//分页

		if (processDefinitions != null && processDefinitions.size() > 0) {
			for (ProcessDefinition processDefinition : processDefinitions) {
				System.out.println("******************************************");
				System.out.println("流程定义ID : " + processDefinition.getId()); // 流程定义的key+版本
																				// +
																				// 随机数
				System.out.println("流程定义的名称  : " + processDefinition.getName());// 对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key : " + processDefinition.getKey());// 对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本 : " + processDefinition.getVersion());// 当流程定义的key相同时版本升级
																					// 默认为1
				System.out.println("资源名称bpmn文件 : " + processDefinition.getResourceName());
				System.out.println("资源名称png文件 : " + processDefinition.getDiagramResourceName());
				System.out.println("部署对象ID : " + processDefinition.getDeploymentId());
				System.out.println("******************************************");
			}
		}
	}

	/** 删除流程 */
	@Test
	public void deleteProcessDefinitionTest() {
		String deploymentId = "1";
		/** 不是级联删除 只能删除没有启动的流程 如果流程已经启动删除会抛错 */
		// processEngine.getRepositoryService().deleteDeployment(deploymentId);
		/** 级联删除 无论流程是否启动都会删除 */
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
		System.out.println("删除成功!");
	}

	/** 查询流程图 */
	@Test
	public void viewProcessDefinitionPngTest() throws IOException {

		String deploymentId = "1101";
		// 获取图片资源的名字
		List<String> deploymentResourceNames = processEngine.getRepositoryService()
				.getDeploymentResourceNames(deploymentId);
		// 定义图片的名字
		String resourceName = "";
		if (deploymentResourceNames != null && deploymentResourceNames.size() > 0) {
			for (String name : deploymentResourceNames) {
				if (name.indexOf(".png") > 0) {
					resourceName = name;
				}
			}
		}
		// 获取图片文件的输入流
		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
		// 生成文件到d盘
		File file = new File("D:/" + resourceName);
		FileUtils.copyInputStreamToFile(in, file);
	}

	/** 查询最新版本的流程定义 */
	@Test
	public void findLastVersionProcessDefinitionTest() {
		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService().createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		if (processDefinitions != null && processDefinitions.size() > 0) {
			for (ProcessDefinition processDefinition : processDefinitions) {
				map.put(processDefinition.getKey(), processDefinition);
			}
		}
		List<ProcessDefinition> pDefinitions = new ArrayList<ProcessDefinition>(map.values());
		if (pDefinitions != null && pDefinitions.size() > 0) {
			for (ProcessDefinition processDefinition : pDefinitions) {
				System.out.println("******************************************");
				System.out.println("流程定义ID : " + processDefinition.getId()); // 流程定义的key+版本 +  随机数
				System.out.println("流程定义的名称  : " + processDefinition.getName());// 对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key : " + processDefinition.getKey());// 对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本 : " + processDefinition.getVersion());// 当流程定义的key相同时版本升级 // 默认为1
				System.out.println("资源名称bpmn文件 : " + processDefinition.getResourceName());
				System.out.println("资源名称png文件 : " + processDefinition.getDiagramResourceName());
				System.out.println("部署对象ID : " + processDefinition.getDeploymentId());
				System.out.println("******************************************");
			}
		}

	}

	/** 根据key 删除 流程定义 */
	@Test
	public void deleteProcessDefinitionBykeyTest() {
		String processDefinitionKey = "helloworld";
		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService()
										.createProcessDefinitionQuery()
										.processDefinitionKey(processDefinitionKey)
										.list();
		if (processDefinitions != null && processDefinitions.size() > 0) {
			for (ProcessDefinition processDefinition : processDefinitions) {
				processEngine.getRepositoryService().deleteDeployment(processDefinition.getDeploymentId(), true);
			}
		}
	}
}
