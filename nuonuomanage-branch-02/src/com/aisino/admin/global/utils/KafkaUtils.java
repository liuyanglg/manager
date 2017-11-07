package com.aisino.admin.global.utils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaUtils {

	private static Properties props;
	private static ProducerConfig config;
	private static Producer<String, String> producer;
	
	static{
		Resource resource = new ClassPathResource("cmp_kafka.properties");
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			config = new ProducerConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessages(String topic,List<String> messages){
		if(messages!=null&&messages.size()>0){
			int s = messages.size();
			producer = new Producer<String, String>(config);
			List<KeyedMessage<String, String>> keyedMessages = new ArrayList<KeyedMessage<String, String>>(s);  
			for (int i = 0; i<s; i++) {
				KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic, i + "", messages.get(i));  
				keyedMessages.add(message);  
			}  
			producer.send(keyedMessages);
		}
	}
	
	public static void sendMessage(String topic,String message){
		if(topic == null || message == null){
            return;
        }
        KeyedMessage<String, String> km = new KeyedMessage<String, String>(topic,message);
        producer = new Producer<String, String>(config);
        producer.send(km);
	}
	
	public static void close(){
		if(producer!=null){
			producer.close();
			producer = null;
		}
	}
}
