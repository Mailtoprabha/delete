package com.example.bulkpayment.ack;

public interface AckService {
    void sendAck(String fileReference, String details);

    void sendNak(String fileReference, String details);
}
