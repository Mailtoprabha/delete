package com.example.bulkpayment.ack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventBridgeAckService implements AckService {

    private static final Logger log = LoggerFactory.getLogger(EventBridgeAckService.class);

    @Override
    public void sendAck(String fileReference, String details) {
        log.info("ACK published for fileReference={}, details={}", fileReference, details);
    }

    @Override
    public void sendNak(String fileReference, String details) {
        log.warn("NAK published for fileReference={}, details={}", fileReference, details);
    }
}
