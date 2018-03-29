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
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author martin.bergljung@alfresco.com
 */
public class HelloWorldExecutionListener implements ExecutionListener {
    private static Logger logger = LoggerFactory.getLogger(HelloWorldExecutionListener.class);

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
        	System.out.println("endTime : "+endTime);
    		}
        }
        
        
         execution.setVariable("var1", stringBuffer.toString());
        String initiator = (String)execution.getVariable("initiator", false);
        logger.info("Initiator of the process has user ID = " + initiator);

        execution.setVariable("greeting1Proc", "Hello World!");
        execution.setVariableLocal("greeting1ProcLocal", "Hello World Local!");

        logger.info("--- Process variables:"+execution.getCurrentActivityName());
        Map<String, Object> procVars = execution.getVariables();
        for (Map.Entry<String, Object> procVar : procVars.entrySet()) {
            logger.info("   [" + procVar.getKey() + " = " + procVar.getValue() + "]");
        }
    }
    
    
    @Resource(name = "formService")
    private FormService formService;
    public void complete(String taskId, Map<String, String> variables){
    	variables.put("var1", "value1");
    	variables.put("var2", "value2");
    	variables.put("var3", "value3");
    	System.out.println("form is being called ");
         formService.submitTaskFormData(taskId, variables);
    }
    
    
    
    
    
    
    
    
}
