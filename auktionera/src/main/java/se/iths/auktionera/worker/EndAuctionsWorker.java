package se.iths.auktionera.worker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.iths.auktionera.business.task.IEndAuctionsTask;

@Component
public class EndAuctionsWorker {

    private static final Logger log = LoggerFactory.getLogger(EndAuctionsWorker.class);
    private final ApplicationContext applicationContext;

    public EndAuctionsWorker(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1000 * 10)
    public void endAuctions() {
        try {
            log.info("Starting work to end auctions.");

            IEndAuctionsTask endAuctionsTask = applicationContext.getBean(IEndAuctionsTask.class);
            endAuctionsTask.endAuctions();

            log.info("Finished work to end auctions.");
        } catch (Exception e) {
            log.error("Something went wrong in endAuctions.", e);
        }


    }
}