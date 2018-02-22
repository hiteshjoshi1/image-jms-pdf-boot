package com.consensys.hitesh.producer.jms.configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
 
@Configuration
public class ActiveMqConnectionFactoryConfig {
 
	@Value("${spring.activemq.broker-url}")
	String brokerUrl;
	
	@Value("${spring.activemq.user}")
	String userName;
	
	@Value("${spring.activemq.password}")
	String password;
 
	@Value("${activemq.image.queue}")
	String queue;
	
	@Bean
	public Queue queue() {
		return new ActiveMQQueue(queue);
	}	
		
	/*
	 * Initial ConnectionFactory
	 */
    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(userName);
        connectionFactory.setPassword(password);
          return connectionFactory;
    }
    
	@Bean // Serialize message content to json using TextMessage
	public MessageConverter messageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	}
 
    

    /*
     * Used for Sending Messages.
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(messageConverter());
        template.setConnectionFactory(connectionFactory());
        return template;
    }
    
    
}