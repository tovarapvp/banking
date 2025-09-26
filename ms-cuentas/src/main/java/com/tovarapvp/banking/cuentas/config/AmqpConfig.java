package com.tovarapvp.banking.cuentas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class AmqpConfig {
  @Bean TopicExchange clientesExchange(){ return new TopicExchange("clientes.exchange"); }
  @Bean TopicExchange movimientosExchange(){ return new TopicExchange("movimientos.exchange"); }

  @Bean Queue clientesQueue(){ return new Queue("clientes.creado.queue", true); }

  @Bean Binding bindingClientes(Queue clientesQueue, TopicExchange clientesExchange){
    return BindingBuilder.bind(clientesQueue).to(clientesExchange).with("cliente.creado");
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
