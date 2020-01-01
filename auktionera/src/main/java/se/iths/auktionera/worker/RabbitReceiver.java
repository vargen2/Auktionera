package se.iths.auktionera.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {
    //todo move to email app
    private static final Logger log = LoggerFactory.getLogger(RabbitReceiver.class);

//    @RabbitListener(queues = AuktioneraApplication.newBidQueue)
//    public void receiveMessage(Message message) {
//        log.info("Received message as generic: {}", message.toString());
//    }

}
