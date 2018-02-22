package com.consensys.hitesh.consumer.jms.configuration;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * @author hitjoshi
 *
 */
@Configuration
public class JMSListenerConnectionFactoryConfig {

	protected Logger logger = LoggerFactory.getLogger(JMSListenerConnectionFactoryConfig.class.getName());

	@Bean
	public MappingJackson2MessageConverter messageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public JmsListenerContainerFactory<?> listenerConnFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setMessageConverter(messageConverter());
		// lambda function
		// JMS listener error handler as a Lamba function
		factory.setErrorHandler(t -> logger.error("An error has occurred in the transaction", t));
		configurer.configure(factory, connectionFactory);
		return factory;
	}

}
