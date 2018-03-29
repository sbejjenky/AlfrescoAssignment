/**
 * Copyright (C) 2016 Alfresco Software Limited.
 * <p/>
 * This file is part of the Alfresco SDK Samples project.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.activiti.extension.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class HistoryExecutionListener implements ExecutionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(HistoryExecutionListener.class);

    @Autowired
    HistoryService historyService;
    
    @Override
    public void notify(DelegateExecution execution) throws Exception {
       
        StringBuffer stringBuffer = new StringBuffer();
        if(historyService == null){
        	historyService= execution.getEngineServices().getHistoryService();
        }
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().list();
        for(HistoricActivityInstance historicActivityInstance: list){
        	String taskId = historicActivityInstance.getProcessInstanceId();
    		if(taskId != null && taskId.equalsIgnoreCase(execution.getProcessInstanceId())){
        	
        	stringBuffer.append("<b> Task Name: </b>"+historicActivityInstance.getActivityName());
        	stringBuffer.append("<b>  -> Task Time: </b>"+historicActivityInstance.getTime());
        	stringBuffer.append("<BR>");
        	Date endTime = historicActivityInstance.getEndTime();
        	logger.info("endTime : "+endTime);
    		}
        }
         execution.setVariable("var1", stringBuffer.toString());
         Map<String, Object> variables = execution.getVariables();
         for(Map.Entry<String, Object> variable : variables.entrySet()){
        	 System.out.println("Started... values");
        	 System.out.println("key: "+variable.getKey()+" : "+variable.getValue()+" value "+ (variable.getValue() != null?variable.getValue().getClass(): null));
         }
         
         FormService formService = execution.getEngineServices().getFormService();
         String id = execution.getEngineServices().getTaskService().createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult().getId();
         System.out.println("id==:"+id);
         TaskFormData taskFormData = formService.getTaskFormData(id);
        System.out.println("=== taskFormData===: "+taskFormData);
         if(taskFormData != null){
         List<FormProperty> formProperties = taskFormData.getFormProperties();
         for(FormProperty formProperty : formProperties){
        
        	 System.out.println("Id: "+formProperty.getId()+" Name: "+formProperty.getName()+" Type"+formProperty.getType()+" Value: "+formProperty.getValue());
         }
         }
         
         
    }
}
