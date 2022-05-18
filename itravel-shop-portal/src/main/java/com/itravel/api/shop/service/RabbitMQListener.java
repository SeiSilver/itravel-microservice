package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.payload.UpdateRateRequest;
import com.itravel.api.shop.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
@RequiredArgsConstructor
@Log4j2
public class RabbitMQListener {
    private final ITravelService iTravelService;
    private final PayloadUtils payloadUtils;

    @RabbitListener(
        queues = "#{itravelUpdateRateQueue}",
        concurrency = "#{messageQueueProperties.listener.concurrency}",
        errorHandler = "processRequestErrorHandler",
        ackMode = "#{messageQueueProperties.listener.ackMode}"
    )
    public void updateRate(Message message) {
        UpdateRateRequest request;
        try {
            request = payloadUtils.parseJson(new String(message.getBody()), UpdateRateRequest.class);
            log.info("Receive update rate for service {}", request.getMainServiceId());
            iTravelService.updateRate(request);
            log.info("Update service rate success");
        } catch (ApplicationException e) {
            log.error("Error: ", e);
        }
    }
}
