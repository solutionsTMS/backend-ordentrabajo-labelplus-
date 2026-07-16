package com.etimet.orden_trabajo.infrastructure.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String ERP_EXCHANGE = "erp.exchange";

    // --- NUEVAS CONSTANTES PARA TAURUS ---
    public static final String TAURUS_CLIENTES_QUEUE = "catalogos.taurus.clientes.queue";
    public static final String TAURUS_CLIENTES_DLQ = "catalogos.taurus.clientes.dlq";

    // La llave con la que Taurus enviará el mensaje (puedes ajustarla según te
    // indiquen)
    public static final String TAURUS_ROUTING_KEY = "taurus.cliente.enviado";
    public static final String TAURUS_DLQ_ROUTING_KEY = "catalogos.taurus.clientes.dlq.key";

    @Bean
    public TopicExchange erpExchange() {
        return new TopicExchange(ERP_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    // ==========================================================
    // COLAS Y CONEXIONES (BINDINGS) PARA ESCUCHAR A TAURUS
    // ==========================================================

    // 1. Cola de Errores (DLQ)
    @Bean
    public Queue taurusClientesDlq() {
        return QueueBuilder.durable(TAURUS_CLIENTES_DLQ).build();
    }

    // 2. Cola Principal (con instrucciones de enviar al DLQ si hay un error
    // crítico)
    @Bean
    public Queue taurusClientesQueue() {
        return QueueBuilder.durable(TAURUS_CLIENTES_QUEUE)
                .withArgument("x-dead-letter-exchange", ERP_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", TAURUS_DLQ_ROUTING_KEY)
                .build();
    }

    // 3. Conectar la Cola Principal al Exchange para recibir los clientes
    @Bean
    public Binding bindingTaurusClientes(Queue taurusClientesQueue, TopicExchange erpExchange) {
        return BindingBuilder.bind(taurusClientesQueue)
                .to(erpExchange)
                .with(TAURUS_ROUTING_KEY);
    }

    // 4. Conectar la Cola de Errores al Exchange para el ruteo interno
    @Bean
    public Binding bindingTaurusClientesDlq(Queue taurusClientesDlq, TopicExchange erpExchange) {
        return BindingBuilder.bind(taurusClientesDlq)
                .to(erpExchange)
                .with(TAURUS_DLQ_ROUTING_KEY);
    }

}
