package com.consensys.hitesh.consumer.jms.configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author hitjoshi
 *
 */
@Configuration
public class JMSConnectionFactoryConfig {

	protected Logger logger = LoggerFactory.getLogger(JMSConnectionFactoryConfig.class.getName());

	@Value("${spring.activemq.broker-url}")
	String brokerUrl;
	
	@Value("${spring.activemq.user}")
	String userName;
	
	@Value("${spring.activemq.password}")
	String password;
 
	@Value("${activemq.pdf.queue}")
	String queue;
	
	@Bean
	public Queue queue() {
		return new ActiveMQQueue(queue);
	}	

	/**
	 * Connection factory for sending JMS message
	 * @return
	 */
	@Bean
	public ConnectionFactory connectionFactory(){
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	    connectionFactory.setBrokerURL(brokerUrl);
	    connectionFactory.setUserName(userName);
	    connectionFactory.setPassword(password);
	    return connectionFactory;
	}
	
	/**
	 * JMS Listener connection factory receive message from Queue
	 * @param connectionFactory
	 * @param configurer
	 * @return
	 */
    @Bean
    public JmsListenerContainerFactory<?> listenerConnFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
	 
	/**
	 * JMS template used for Sending Messages.
	 * @return {@link JmsTemplate}
	 */
	@Bean
	public JmsTemplate jmsTemplate(){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(connectionFactory());
	    return template;
	}	

}
