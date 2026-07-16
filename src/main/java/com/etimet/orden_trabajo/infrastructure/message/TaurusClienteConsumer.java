package com.etimet.orden_trabajo.infrastructure.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.etimet.orden_trabajo.application.services.ClientesService;
import com.etimet.orden_trabajo.domain.records.eventos.ClientesMensajeEvento;
import com.etimet.orden_trabajo.infrastructure.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaurusClienteConsumer {
    private final ClientesService clientesService;

    @RabbitListener(queues = RabbitMQConfig.TAURUS_CLIENTES_QUEUE)
    public void recibirClienteDesdeTaurus(ClientesMensajeEvento mensaje) {
        log.info("Cliente recibido desde Taurus en cola {}: {}", RabbitMQConfig.TAURUS_CLIENTES_QUEUE, mensaje);
        clientesService.procesarClienteDesdeTaurus(mensaje);
    }

}
