/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.bot.spring.bean.Person;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	
	
	private static Map<String,Person> personMap;
	
	static{
		personMap = new HashMap<String,Person>();
	}
	
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        final String originalMessageText = event.getMessage().getText();
        
        String lineId = event.getSource().getUserId();
        Person person = null;
        if(lineId != null && !lineId.isEmpty()){
        	person = getPersonById(lineId);
        }
        
        boolean isSetting = false;
        if(person != null ){
        	isSetting = "Y".equals(person.getIsSetting());
        }
        
        TextMessage result = null;
        
        if("87".equals(originalMessageText)){
        	result =  new TextMessage("你全家都 87");
        	
        }else if("[HY]綁定ID".equals(originalMessageText)){
        	if(person != null){
        		person.setIsSetting("Y");
        	}else{
        		person = new Person();
        		person.setLineId(lineId);
        		person.setIsSetting("Y");
        	}
        	
        	boolean isSuccess = savePerson(person);
        	
        	if(isSuccess){
        		result =  new TextMessage("請輸入要綁定的名稱");
        	}else{
        		result =  new TextMessage("系統錯誤，請再試一次");
        	}
        	
        }else if(isSetting && originalMessageText != null){
        	
        	if(person == null){
        		result =  new TextMessage("系統錯誤，請再試一次");
        	}else{
        		person.setName(originalMessageText);;
        		person.setIsSetting("N");
        		boolean isSuccess = savePerson(person);
        		
        		if(isSuccess){
        			result =  new TextMessage(String.format("已為您綁定的名稱 : %s", originalMessageText));
        		}else{
        			result =  new TextMessage("系統錯誤，請再試一次");
        		}
        	}
        	
        	
        }else if("[HY]查詢名稱".equals(originalMessageText)){
        	result =  new TextMessage(String.format("您的名子為 : %s", person.getName()));
        }else if("[HY]查詢ID".equals(originalMessageText)){
        	result =  new TextMessage(String.format("您的 ID 為 : %s", person.getLineId()));
        }else{
        	result =  new TextMessage(String.format("這個我看不懂喔 : %s", originalMessageText));
        }
        return result;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
    
    private boolean savePerson(Person person){
    	
    	boolean result = false;
    	
    	
    	try {
    		personMap.put(person.getLineId(), person);
			result = true;
			
			return result;
		} catch (Exception e) {
			return false;
		}

    }
    
	private Person getPersonById(String lineId) {

		Person person = null;
		
		try {
			person = personMap.get(lineId);
			return person;
		} catch (Exception e) {
			return person;
		}
	}
}
