package za.co.dearx.leave.scheduler;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import za.co.dearx.leave.service.PublicHolidayService;
import za.co.dearx.leave.service.exception.UpdateException;

public class PublicHolidayUpdate {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayUpdate.class);

    final PublicHolidayService service;

    public PublicHolidayUpdate(PublicHolidayService service) {
        this.service = service;
    }

    /**
     * Scheduler to update the public holidays every day at 1am.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void update() {
        final LocalDate now = LocalDate.now();
        try {
            service.reloadData(now.getYear());
            log.debug("Public holidays updated using scheduler");
        } catch (UpdateException e) {
            log.debug("Failed to update public holidays using scheduler", e.getReason());
        }
    }
}
